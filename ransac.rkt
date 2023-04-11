#lang scheme
(define (readXYZ fileIn)
 (let ((sL (map (lambda s (string-split (car s)))
                (cdr (file->lines fileIn)))))
 (map (lambda (L)
        (map (lambda (s)
               (if (eqv? (string->number s) #f)
               s
               (string->number s))) L)) sL)))

;the principal function:
(define (planeRANSAC filename confidence percentage eps)
  (define k (ransacNumberOfIterations confidence percentage))
  (define Ps (readXYZ filename))
  (define supp (support 0 (dominantPlane '(1 1 1 1) Ps k eps) Ps eps))
  (println "(support a-coordinate b-coordinate c-coordinate d-coordinate)")
  (println supp)
)

;computes a plane equation ax+by+cz=d from 3 points.
(define (plane P1 P2 P3)
  ;normal vector = cross product of direction vectors v1 and v2
  (define v1 (list (- (list-ref P1 0) (list-ref P2 0)) (- (list-ref P1 1) (list-ref P2 1)) (- (list-ref P1 2) (list-ref P2 2)) ))
  (define v2 (list (- (list-ref P2 0) (list-ref P3 0)) (- (list-ref P2 1) (list-ref P3 1)) (- (list-ref P2 2) (list-ref P3 2)) ))
  (define a (- (* (list-ref v1 1) (list-ref v2 2)) (* (list-ref v2 1) (list-ref v1 2))))
  (define b (- (* (list-ref v1 2) (list-ref v2 0)) (* (list-ref v2 2) (list-ref v1 0))))
  (define c (- (* (list-ref v1 0) (list-ref v2 1)) (* (list-ref v2 0) (list-ref v1 1))))
  (define d (+ (* -1 a (list-ref P1 0)) (* -1 b (list-ref P1 1)) (* -1 c (list-ref P1 2))))
  (list a b c d))

;computes the distance between a plane and a point
(define (distance plane point)
        (define a (list-ref plane 0))
        (define b (list-ref plane 1))
        (define c (list-ref plane 2))
        (define d (list-ref plane 3))
        (define numerator (+ (* a (list-ref point 0)) (* b (list-ref point 1)) (* c (list-ref point 2)) d))
	(define denominator (sqrt (+ (expt a 2) (expt b 2) (expt c 2))))
	(abs (/ numerator denominator)))

;Count the support of a plane. Returns support count and plane parameter in a pair
;ASSUME supportingpoints IS ORIGINALLY SET TO 0
(define (support currentSupport currentPlane points eps)
        (if (null? points)
            (cons currentSupport currentPlane) ;return
        ;else
            (if (< (distance currentPlane (car points)) eps)
                (support (+ 1 currentSupport) currentPlane (cdr points) eps) ;increment current support
            ;else
                (support currentSupport currentPlane (cdr points) eps)
            )
            
        )
)

;Repeat the random sampling K times to find the dominant plane (the plane with the best support).
(define (dominantPlane bestPlane Ps k eps)
  (define thisPlane (plane (list-ref Ps (random (length Ps))) (list-ref Ps (random (length Ps))) (list-ref Ps (random (length Ps))))) ;create a plane of three random points
  
  (if (<= k 0)
      bestPlane
  ;else
      (if (> (list-ref (support 0 thisPlane Ps eps) 0) (list-ref (support 0 bestPlane Ps eps) 0));if new plane has a better support
          (dominantPlane thisPlane Ps (- k 1) eps)
      ;else
          (dominantPlane bestPlane Ps (- k 1) eps)
      )
  )
  
)

;Computes number of iterations required based on confidence and percentage of points
(define (ransacNumberOfIterations confidence percentage)
  (ceiling (/ (log (- 1 confidence)) (log (- 1 (expt percentage 3) )) ) )
)

;run RANSAC
;(planeRANSAC "Point_Cloud_1_No_Road_Reduced.xyz" 0.8 0.1 0.3)
;(planeRANSAC "Point_Cloud_2_No_Road_Reduced.xyz" 0.8 0.1 0.3)
;(planeRANSAC "Point_Cloud_3_No_Road_Reduced.xyz" 0.8 0.1 0.3)