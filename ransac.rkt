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
(define (planeRANSAC filename confidence percentage eps) "RETURN")

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
;ASSUME support SUPPORT IS ORIGINALLY SET TO 0
(define (support support plane points eps)
        ;(list-ref points (random (length points)))

        (if (null? points)
            (support plane) ;return
            (
              (let ((p (car points)))
                (if (< (distance plane p) eps)
                    (set! support (+ support 1)) ;increment current support
                ;else
                    (set! support support);does nothing
                    )
                )
              (support support plane (cdr points) eps) ;recurse to the next point in the cloud and find current support)
            )
        )
)

;Repeat the random sampling K times to find the dominant plane (the plane with the best support).
(define (dominantPlane Ps k eps)
  (if ()))

;Computes number of iterations required based on confidence and percentage of points
(define (ransacNumberOfIterations confidence percentage) "RETURN")

;main
(define A '(1 1 4))
(define B '(3 2 0))
(define C '(0 -1 1))
(plane A B C)

(distance '(1 2 0 -1) '(4 3 6))