package com.rybindev.weatherapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybindev.weatherapp.interceptor.TransactionInterceptor;
import com.rybindev.weatherapp.mapper.LocationMapper;
import com.rybindev.weatherapp.mapper.UserDtoMapper;
import com.rybindev.weatherapp.mapper.UserMapper;
import com.rybindev.weatherapp.repository.LocationRepository;
import com.rybindev.weatherapp.repository.UserRepository;
import com.rybindev.weatherapp.service.UserLocationService;
import com.rybindev.weatherapp.service.UserService;
import com.rybindev.weatherapp.service.WeatherService;
import com.rybindev.weatherapp.util.HibernateUtil;
import com.rybindev.weatherapp.validator.CreateUserValidator;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.lang.reflect.Proxy;
import java.net.http.HttpClient;

public class ApplicationContext {

    public static final SessionFactory sessionFactory;
    public static final Session session;
    public static final TransactionInterceptor transactionInterceptor;
    public static final UserRepository userRepository;
    public static final LocationRepository locationRepository;
    public static final UserMapper userMapper;
    public static final UserDtoMapper userDtoMapper;
    public static final CreateUserValidator createUserValidator;
    public static final LocationMapper locationMapper;
    public static final ObjectMapper objectMapper;
    public static final HttpClient httpClient;
    public static final WeatherService weatherService;
    public static final UserService userService;
    public static final UserLocationService userLocationService;

    static {
        sessionFactory = sessionFactory();
        session = session(sessionFactory);
        transactionInterceptor = new TransactionInterceptor(sessionFactory);
        userRepository = userRepository(session);
        locationRepository = locationRepository(session);
        userMapper = new UserMapper();
        userDtoMapper = new UserDtoMapper();
        createUserValidator = new CreateUserValidator();
        locationMapper = new LocationMapper();
        objectMapper = new ObjectMapper();
        httpClient = HttpClient.newHttpClient();
        weatherService = weatherService(httpClient, objectMapper);
        userService = userService(userRepository, createUserValidator, userMapper, userDtoMapper, transactionInterceptor);
        userLocationService = userLocationService(userRepository, locationRepository, locationMapper, weatherService, transactionInterceptor);

    }

    private static SessionFactory sessionFactory() {
        return HibernateUtil.getSessionFactory();
    }

    private static Session session(SessionFactory sessionFactory) {
        return (Session) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{Session.class},
                (proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(), args));
    }

    private static UserRepository userRepository(Session session) {
        return new UserRepository(session);
    }

    private static LocationRepository locationRepository(Session session) {
        return new LocationRepository(session);
    }

    private static WeatherService weatherService(HttpClient httpClient,
                                                 ObjectMapper objectMapper) {
        return new WeatherService(httpClient, objectMapper);
    }

    private static UserLocationService userLocationService(UserRepository userRepository,
                                                           LocationRepository locationRepository,
                                                           LocationMapper locationMapper,
                                                           WeatherService weatherService,
                                                           TransactionInterceptor transactionInterceptor) {
        try {
            return new ByteBuddy()
                    .subclass(UserLocationService.class)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(transactionInterceptor))
                    .make()
                    .load(Thread.currentThread().getContextClassLoader())
                    .getLoaded()
                    .getConstructor(
                            UserRepository.class,
                            LocationRepository.class,
                            LocationMapper.class,
                            WeatherService.class)
                    .newInstance(
                            userRepository,
                            locationRepository,
                            locationMapper,
                            weatherService);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static UserService userService(UserRepository userRepository,
                                           CreateUserValidator createUserValidator,
                                           UserMapper userMapper,
                                           UserDtoMapper userDtoMapper,
                                           TransactionInterceptor transactionInterceptor) {
        try {
            return new ByteBuddy()
                    .subclass(UserService.class)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(transactionInterceptor))
                    .make()
                    .load(Thread.currentThread().getContextClassLoader())
                    .getLoaded()
                    .getConstructor(
                            UserRepository.class,
                            CreateUserValidator.class,
                            UserMapper.class,
                            UserDtoMapper.class)
                    .newInstance(
                            userRepository,
                            createUserValidator,
                            userMapper,
                            userDtoMapper);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
