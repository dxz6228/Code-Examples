// 
// File: mmult.c
//
// @author Denis Zhenilov <dxz6228>
// // // // // // // // // // // // // // // // // // // // // // // 

#include "mmult.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <strings.h>
#include <ctype.h>

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
double **mmult(int rA, int cA, double **A, int rB, int cB, double **B){
	int row;
	row = 0;
	double** newMatrix;
	newMatrix = (double **) malloc(sizeof(double*)*rA);
	while(row<rA){
		int col;
		col=0;
		newMatrix[row] = (double *) malloc(sizeof(double)*cB);
		while(col<cB){
			double elm;
			elm = multiplyElms(A, B, row, col, cA);
			newMatrix[row][col]=elm;
			col++;
		}
		row++;
	}
	return newMatrix;
}

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
double multiplyElms(double **A, double **B, int rA, int cB, int numEls){
	double elm;
	elm = 0.0;
	int counter;
	counter=0;
	while(counter<numEls){
		elm = elm + (A[rA][counter] * B[counter][cB]);
		counter++;
	}
	return elm;
}

/*
 * This function simply frees all memory allocated for a matrix A.
 * Args:
 * r - number of rows in A.
 * c - number of columns in A
 * A - a matrix to be freed.
 */
void xfree(int r, int c, double **A){
	for(int i=0; i<r; i++){
		free(A[i]);
	}
	free(A);
}

/*
 * Reads a matrix, in row-major order, from a file fp and returns
 * it.
 * Args:
 * fp - a pointer to a file stream from which to read
 * r - number of rows in a matrix
 * c - number of columns in a matrix
 */
double **mread(FILE *fp, int r, int c){
	double** matrix;
	matrix = (double **) malloc(sizeof(double*) * r);
	int row;
	row=0;
	while(row<r){
		int col;
		col=0;
		char *line = NULL;
		size_t len = 0;
		getline(&line, &len, fp);
		char *pch;
		pch = strtok(line, " ");
		while(pch != NULL && col<c){
			if(col==0){
				matrix[row] = (double *) malloc(sizeof(double)*c);
			}
			char *ptr;
			matrix[row][col]=strtod(pch, &ptr);
			col++;
			pch = strtok(NULL, " ");
		}
		row++;
		free(line);
	}
	return matrix;	
}

/*
 * Writes a matrix to an already open file fp.
 * Args:
 * fp - a pointer to a file stream to which to write
 * rA - number of rows in a matrix
 * cA - number of columns in a matrix
 * A - the matrix to be written to a file
 */
void mwrite(FILE *fp, int rA, int cA, double **A){
	int row;
	row=0;
	fprintf(fp, "%d %d\n", rA, cA);
	while(row<rA){
		int col;
		col=0;
		while(col<cA){
			fprintf(fp, "%8.2lf", A[row][col]);
			if(col<cA-1){
				fprintf(fp, " ");
			}
			col++;
		}
		fprintf(fp, "\n");
		row++;
	}
}
