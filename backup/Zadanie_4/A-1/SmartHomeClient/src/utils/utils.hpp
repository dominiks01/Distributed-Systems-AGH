#ifndef MY_UTILS
#define MY_UTILS

#include <iostream>

constexpr unsigned int str2int(const std::string& str, int h = 0)
{
    return !str[h] ? 5381 : (str2int(str, h+1) * 33) ^ str[h];
}

#endif