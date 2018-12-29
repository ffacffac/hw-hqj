package com.hw.adb;

import java.util.List;

import com.hw.adb.Eenum.ShellType;

public interface IADBListener
{
    void findDevice(List<String> devices);

    void filePro(String pro, ShellType shellType);

    void shellException(ShellType shellType, String shellSrc, Exception e);

    void close();
}
