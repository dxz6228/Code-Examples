// 
// File: mmult.h 
//
// @author Denis Zhenilov <dxz6228>
// // // // // // // // // // // // // // // // // // // // // // // 

#ifndef MMULT_H
#define MMULT_H

#include <stdio.h>

/*
 * This function multiplies out two matrices, A and B, and returns a newly
 * alloc'd matrix that is the result of multiplication.
 * Args:
 * rA - number of rows in A
 * cA - number of columns in A
 * A - first matrix to be multiplied
 * rB - number of rows in B
 * cB - number of columns in B
 * B - second matrix to be multiplied
 * 
 * It assumes that the input is valid.
 */
double **mmult(int rA, int cA, double **A, int rB, int cB, double **B);

/*
 * A helper function that multiplies given row in matrix A by a given
 * column in matrix B and returns the resultant member of a new
 * matrix. It is called by mmult.
 * Args:
 * A - the matrix that provides rows
 * B - the matirx that provides columns
 * rA - the row of A to be multiplied
 * cB - the column of B to be multiplied
 * numElms - number of elements in the row of A / column of B
 */
double multiplyElms(double **A, double **B, int rA, int cB, int numEls);

/*
 * This function simply frees all memory allocated for a matrix A.
 * Args:
 * r - number of rows in A.
 * c - number of columns in A
 * A - a matrix to be freed.
 */
void xfree(int r, int c, double **A);

/*
 * Reads a matrix, in row-major order, from a file fp and returns
 * it.
 * Args:
 * fp - a pointer to a file stream from which to read
 * r - number of rows in a matrix
 * c - number of columns in a matrix
 */
double **mread(FILE *fp, int r, int c);

/*
 * Writes a matrix to an already open file fp.
 * Args:
 * fp - a pointer to a file stream to which to write
 * rA - number of rows in a matrix
 * cA - number of columns in a matrix
 * A - the matrix to be written to a file
 */
void mwrite(FILE *fp, int rA, int cA, double **A);

#endif