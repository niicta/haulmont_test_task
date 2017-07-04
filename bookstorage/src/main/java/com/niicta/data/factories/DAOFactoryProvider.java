package com.niicta.data.factories;

import com.niicta.data.factories.impl.GenericHSQLDBInProgressDAOFactory;

/**
 * Singleton class for providing different DAOFactories
 */
public class DAOFactoryProvider {
    public static final int GENERIC_HSQLDB_IN_PROGRESS_FACTORY = 1;

    private static DAOFactoryProvider provider = new DAOFactoryProvider();
    private DAOFactoryProvider(){}

    /**
     * provides <code>DAOFactory</code> which type depends on input parameter
     * @param type is static final constant of <code>DAOFactoryProvider</code> class, for example
     * <code>DAOFactoryProvider.GENERIC_HSQLDB_IN_PROGRESS_FACTORY</code>
     * @return <code>GenericHSQLDBInProgressDAOFactory</code> if <code>type</code> is <code>GENERIC_HSQLDB_IN_PROGRESS_FACTORY</code>
     * @throws RuntimeException in case of specified type not found
     */
    public DAOFactory geDAOFactoryByType(int type){
        if (type == GENERIC_HSQLDB_IN_PROGRESS_FACTORY){
            return new GenericHSQLDBInProgressDAOFactory();
        }
        else throw new RuntimeException("Illegal factory type");
    }

    /**
     * provides <code>DAOFactory</code> with default type
     * @return <code>GenericHSQLDBInProgressDAOFactory</code>
     */
    public DAOFactory getDefaultDAOFactory(){
        return geDAOFactoryByType(GENERIC_HSQLDB_IN_PROGRESS_FACTORY);
    }

    public static DAOFactoryProvider getProvider(){
        return provider;
    }
}
