BEGIN {
    print "Roster:\n";
}
$1 <= 5 {
    print $2;
    print length($3) "\n"
}
$1 > 6 {
    print $3 "\n"
}
