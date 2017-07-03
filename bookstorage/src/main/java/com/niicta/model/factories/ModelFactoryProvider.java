package com.niicta.model.factories;

import com.niicta.model.factories.impl.GenericModelFactory;

public class ModelFactoryProvider {
    public static final int GENERIC_FACTORY = 1;

    private static ModelFactoryProvider provider = new ModelFactoryProvider();
    private ModelFactoryProvider(){}

    public ModelFactory getModelFactoryByType(int type){
        if (type == GENERIC_FACTORY){
            return new GenericModelFactory();
        }
        else throw new RuntimeException("Illegal factory type");
    }

    public ModelFactory getDefaultModelFactory(){
        return getModelFactoryByType(GENERIC_FACTORY);
    }

    public static ModelFactoryProvider getProvider(){
        return provider;
    }
}
