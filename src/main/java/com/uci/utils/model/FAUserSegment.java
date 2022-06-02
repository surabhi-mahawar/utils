package com.uci.utils.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class FAUserSegment {
    public FADevice device;
    public ArrayList<FAUser> users;
}
