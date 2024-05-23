
#ifndef CALC_ICE
#define CALC_ICE

module Demo
{
  enum operation { MIN, MAX, AVG };
  exception NoInput {};
  sequence<int> values;

  interface Calc {
    long add(int a, int b);
    long subtract(int a, int b);
    void print(Demo::values list);
  };

};

#endif
