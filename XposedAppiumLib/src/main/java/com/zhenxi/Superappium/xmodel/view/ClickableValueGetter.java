package com.zhenxi.Superappium.xmodel.view;

import com.zhenxi.Superappium.SuperAppium;
import com.zhenxi.Superappium.ViewImage;
import com.zhenxi.Superappium.xmodel.ValueGetter;

public class ClickableValueGetter implements ValueGetter<Boolean> {

    @Override
    public Boolean get(ViewImage viewImage) {
        return viewImage.getOriginView().isClickable();
    }

    @Override
    public boolean support(Class type) {
        return true;
    }

    @Override
    public String attr() {
        return SuperAppium.clickable;
    }
}
