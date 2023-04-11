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

%Vrai si Point3 est un triplet de points al�atoires
random3points(Points, [P1,P2,P3]):- member(P1,Points), member(P2,Points), member(P3,Points), CANNOT BE EQUAL AND MUST MAKE RANDOM NUMBERS.
%Vrai si Plane est l��quation du plan passant par les trois points
plane([[X1,Y1,Z1],[X2,Y2,Z2],[X3,Y3,Z3]], [A,B,C,D]):-
 %components on two normal vectors
 V1X is X1-X2,
 V1Y is Y1-Y2,
 V1Z is Z1-Z2,
 V2X is X2-X3,
 V2Y is Y2-Y3,
 V2Z is Z2-Z3,
 %cross product of those vectors
 A is V1Y*V2Z - V2Y*V1Z,
 B is V1Z*V2X - V2Z*V1X,
 C is V1X*V2Y - V2X*V1Y,
 %rearrange to solve for final variable
 D is -1*A*X1 -1*B*Y1 -1*C*Z1.

% vrai si le support du Plane est compos� de N points de la liste Points lorsque la distance Eps est utilis�e
support(Plane, [Point|Points], Eps, N):- distance(Plane, Point, X), X < eps, N is N+1, support(Plane, Points, Eps, N).
support(Plane, [Point|Points], Eps, N):- distance(Plane, Point, X), X >= eps, support(Plane, Points, Eps, N).
support(_, [], _, _).

distance([X,Y,Z], [A,B,C,D], N) :-
 N1 is A*X + B*Y + C*Z + D,
 N2 is A*A + B*B + C*C,
 Numerator is abs(N1),
 Denominator is sqrt(N2),
 N is Numerator/Denominator.

% vrai si N est le nombre d�it�rations requis par RANSAC avec les param�tres Confidence et Percentage
ransac-number-of-iterations(Confidence, Percentage, N):- N is ceiling( log( 1 - Confidence ) / log( 1 - Percentage**3 ) ).

%les cas de test
test(ransac-number-of-iterations, 1) :- ransac-number-of-iterations(0.8,0.1,N), N =:= 1609.
test(plane, 1) :- plane([[0,1,2],[3,-1,2],[4,3,0]],[A,B,C,D]), A =:= 4, B =:= 6, C =:= 14, D =:= 34.
test(support, 1) :- support([0,0,1,0], [[1,0,0], [2,0,0], [44,44,44]], 2, N), N =:= 2.
test(random3points, 1) :- random3points(Points, [P1,P2,P3]), member(P1,Points), member(P2,Points), member(P3,Points), P1 \= P2, P2 \= P3.
