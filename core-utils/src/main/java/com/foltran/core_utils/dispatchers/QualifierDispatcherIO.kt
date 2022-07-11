package com.foltran.core_utils.dispatchers

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue

object QualifierDispatcherIO : Qualifier {
    override val value: QualifierValue
        get() = "QualifierDispatcherIO"
}
