package com.niicta.model.factories;

import com.niicta.model.factories.impl.GenericModelFactory;

/**
 * Singleton class for providing different ModelFactories
 */
public class ModelFactoryProvider {
    public static final int GENERIC_FACTORY = 1;

    private static ModelFactoryProvider provider = new ModelFactoryProvider();
    private ModelFactoryProvider(){}

    /**
     * provides <code>ModelFactory</code> which type depends on input parameter
     * @param type is static final constant of <code>ModelFactoryProvide</code> class, for example
     * <code>ModelFactoryProvider.GENERIC_FACTORY</code>
     * @return <code>GenericModelFactory</code> if <code>type</code> is <code>GENERIC_FACTORY</code>
     * @throws RuntimeException in case of specified type not found
     */
    public ModelFactory getModelFactoryByType(int type){
        if (type == GENERIC_FACTORY){
            return new GenericModelFactory();
        }
        else throw new RuntimeException("Illegal factory type");
    }

    /**
     * provides <code>ModelFactory</code> with default type
     * @return <code>GenericModelFactory</code>
     */
    public ModelFactory getDefaultModelFactory(){
        return getModelFactoryByType(GENERIC_FACTORY);
    }

    public static ModelFactoryProvider getProvider(){
        return provider;
    }
}
