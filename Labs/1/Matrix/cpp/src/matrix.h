#ifndef MATRIX_MATRIX_H_
#define MATRIX_MATRIX_H_

#include <cmath>
#include <cstring>
#include <iostream>

class Matrix {
 public:
  Matrix();
  Matrix(int rows, int cols);
  Matrix(const Matrix &other);
  Matrix(Matrix &&other) noexcept;
  ~Matrix();

  int getRows() const noexcept;
  int getCols() const noexcept;
  void setRows(const int rows);
  void setCols(const int cols);
  bool EqMatrix(const Matrix &other) const noexcept;
  void SumMatrix(const Matrix &other);
  void SubMatrix(const Matrix &other);
  void MulNumber(const double num) noexcept;
  void MulMatrix(const Matrix &other);
  Matrix Transpose() const noexcept;
  double Determinant() const;
  Matrix CalcComplements() const;
  Matrix InverseMatrix() const;

  double &operator()(int i, int j) const;

  Matrix operator+(const Matrix &other) const noexcept;
  Matrix operator-(const Matrix &other) const noexcept;
  Matrix operator*(const Matrix &other) const noexcept;
  Matrix operator*(const double num) const noexcept;
  bool operator==(const Matrix &other) const noexcept;

  Matrix &operator*=(const Matrix &other) noexcept;
  Matrix &operator*=(const double num) noexcept;
  Matrix &operator+=(const Matrix &other) noexcept;
  Matrix &operator-=(const Matrix &other) noexcept;
  Matrix &operator=(const Matrix &other);
  Matrix &operator=(Matrix &&other) noexcept;

 private:
  double **matrix_;
  int rows_, cols_;
  Matrix Minor(int row, int column) const noexcept;
  void AllocateMatrix();
};

#endif  //MATRIXPLUS_MATRIX_H_