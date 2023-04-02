%predicat qui lit le PC dans un fichier et cree une liste des Points3D
read_xyz_file(File, Points) :-
 open(File, read, Stream),
 read_xyz_points(Stream,Points),
 close(Stream).
read_xyz_points(Stream, []) :-
 at_end_of_stream(Stream).
read_xyz_points(Stream, [Point|Points]) :-
 \+ at_end_of_stream(Stream),
 read_line_to_string(Stream,L), split_string(L, "\t", "\s\t\n",
XYZ), convert_to_float(XYZ,Point),
 read_xyz_points(Stream, Points).
convert_to_float([],[]).
convert_to_float([H|T],[HH|TT]) :-
 atom_number(H, HH),
 convert_to_float(T,TT).

random3points(Points, Point3).
plane(Point3 , Plane).
support(Plane, Points, Eps, N).
ransac-number-of-iterations(Confidence, Percentage, N).

%les cas de test
test(reverse, 1) :- reverse([a,b,c], [c,b,a]).
test(reverse, 2) :- reverse([a], [a]).
test(reverse, 3) :- reverse([], []).
