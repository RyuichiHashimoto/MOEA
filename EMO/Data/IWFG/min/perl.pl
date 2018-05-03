


for ($i = 0 ; $i < 100;$i ++){
	$output = "DTLZ3OBJ_" .$i.".csv";
	open(INPUT,"<","DTLZ3OBJ.csv");
	open(OUT,">",$output);
	
	$line = <INPUT>;
	print OUT $line;
	$line = <INPUT>;
	print OUT $line;
	$line = <INPUT>;
	print OUT 90;
	print OUT "\n";
	
	$counter = 0;
	while(my $line = <INPUT>){
			if($counter != $i){
				print OUT $line
			}
			$counter++;
	
	}

}
