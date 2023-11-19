function factorial (number){
    if(number == 1){
        return 1;
    } 
    else{
        return number * factorial(number-1)
    }
}

BEGIN {
    print factorial(5) "\n"
}