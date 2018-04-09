// 
// File: mmult-driver.c 
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
 * A main function that reads the first matrix out of a file stream;
 * by default - stdin, but it can be easily modified to open other
 * file streams and read from them.
 * It will first read the number of matrices in a file - if there
 * are any, and number provided is valid, it will proceed to scan
 * the first matrix by reading the number of rows and columns in
 * it, and calling mread from mmult.c
 * If there are more than one matrix in a file, it will multiply
 * them in order, starting from the first matrix multiplied by the
 * one that follows it, until there are no more matrices.
 * It prints the last matrix with mwrite to stdout, which can also
 * be changed to write a matrix to a different file stream.
 */
int main(void){
	int matricesLeft;
	double **finalMatrix;
	int finalRows;
	int finalCols;
	char *line = NULL;
	size_t len = 0;
	getline(&line, &len, stdin);
	char *endptrMatricesLeft;
	matricesLeft = (int) strtol(line, &endptrMatricesLeft, 10);
	if(matricesLeft>0){
		getline(&line, &len, stdin);
		char *pch;
		pch = strtok(line, " ");
		char *ptrRows;
		char *ptrCols;
		finalRows = (int) strtol(pch, &ptrRows, 10);
		pch = strtok(NULL, " ");
		finalCols = (int) strtol(pch, &ptrCols, 10);
		finalMatrix = mread(stdin, finalRows, finalCols);
		matricesLeft--;
		while(matricesLeft>0){
			double **otherMatrix;
			int otherRows;
			int otherCols;
			getline(&line, &len, stdin);
			char *pchOther;
			pchOther = strtok(line, " ");
			char *ptrOtherRows;
			char *ptrOtherCols;
			otherRows = (int) strtol(pchOther, &ptrOtherRows, 10);
			pchOther = strtok(NULL, " ");
			otherCols = (int) strtol(pchOther, &ptrOtherCols, 10);
			otherMatrix = mread(stdin, otherRows, otherCols);
			double **temp;
			temp = finalMatrix;
			finalMatrix = mmult(finalRows, finalCols, temp, otherRows, otherCols, otherMatrix);
			xfree(otherRows, otherCols, otherMatrix);
			xfree(finalRows, finalCols, temp);
			finalCols=otherCols;
			matricesLeft--;
		}
		mwrite(stdout, finalRows, finalCols, finalMatrix);
		xfree(finalRows, finalCols, finalMatrix);
		free(line);
		return EXIT_SUCCESS;
	}
	free(line);
	return EXIT_FAILURE;
}