


for ($i = 0 ; $i < 100;$i ++){
	$output = "greedyselection3_" .$i.".csv";
	open(INPUT,"<","greedyselection3.csv");
	open(OUT,">",$output);
	
	$line = <INPUT>;
	print OUT $line;
	$line = <INPUT>;
	print OUT $line;
	$line = <INPUT>;
	print OUT 99;
	print OUT "\n";
	
	$counter = 0;
	while(my $line = <INPUT>){
			if($counter != $i){
				print OUT $line
			}
			$counter++;
	
	}

}
