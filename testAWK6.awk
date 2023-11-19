BEGIN {
    #print "test for loooop\n"
    for(i=0; i<10; i++){
        print i "\n";
    }

    thing = 2
    while (thing > -8){
        print --thing "\n"
    }
}