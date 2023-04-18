package com.acl.stock.util;

public interface ChecksumImplementation {

    @SuppressWarnings("unused")
    void generateChecksum();

    void validate();

    String digest();
}
