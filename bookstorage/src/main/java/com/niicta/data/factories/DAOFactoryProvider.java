package com.niicta.data.factories;

import com.niicta.data.factories.impl.GenericHSQLDBInProgressDAOFactory;

public class DAOFactoryProvider {
    public static final int GENERIC_HSQLDB_IN_PROGRESS_FACTORY = 1;

    private static DAOFactoryProvider provider = new DAOFactoryProvider();
    private DAOFactoryProvider(){}

    public DAOFactory geDAOFactoryByType(int type){
        if (type == GENERIC_HSQLDB_IN_PROGRESS_FACTORY){
            return new GenericHSQLDBInProgressDAOFactory();
        }
        else throw new RuntimeException("Illegal factory type");
    }

    public DAOFactory getDefaultDAOFactory(){
        return geDAOFactoryByType(GENERIC_HSQLDB_IN_PROGRESS_FACTORY);
    }

    public static DAOFactoryProvider getProvider(){
        return provider;
    }
}
