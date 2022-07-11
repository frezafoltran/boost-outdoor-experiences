package com.foltran.core_utils.distance


fun Double.getDistanceBucket(): Int =
    if (this >= 42000) 42
    else if (this >= 20000) 20
    else if (this >= 10000) 10
    else if (this >= 5000) 5
    else if (this >= 3000) 3
    else 0