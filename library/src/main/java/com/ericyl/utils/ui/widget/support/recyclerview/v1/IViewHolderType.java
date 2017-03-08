package com.ericyl.utils.ui.widget.support.recyclerview.v1;

import java.io.Serializable;

public interface IViewHolderType extends Serializable, Cloneable {

    int HEADER = 1;
    int BODY = 2;
    int FOOTER = 3;

    int type();
}
