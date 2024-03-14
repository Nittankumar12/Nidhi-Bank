package com.Railworld.NidhiBank.model;

import jakarta.persistence.OneToMany;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    USER,
    ADMIN
}
