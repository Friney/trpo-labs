#include "matrix.h"

#include <cmath>
#include <cstring>
#include <iostream>

Matrix::Matrix() : matrix_(nullptr), rows_(0), cols_(0) {}

Matrix::Matrix(int rows, int cols) : rows_(rows), cols_(cols) {
  if (rows < 1 || cols < 1)
    throw std::length_error(
        "Invalid input, matrices must have a positive size");
  try {
    this->AllocateMatrix();
  } catch (std::bad_alloc &e) {
    throw e;
  }
}

Matrix::Matrix(const Matrix &other)
    : rows_(other.rows_), cols_(other.cols_) {
  try {
    this->AllocateMatrix();
  } catch (std::bad_alloc &e) {
    throw e;
  }
  for (int i = 0; i < rows_; ++i) {
    memcpy(matrix_[i], other.matrix_[i], cols_ * sizeof(double));
  }
}

Matrix::Matrix(Matrix &&other) noexcept
    : matrix_(other.matrix_), rows_(other.rows_), cols_(other.cols_) {
  other.rows_ = 0;
  other.cols_ = 0;
  other.matrix_ = nullptr;
}

Matrix::~Matrix() noexcept {
  for (int i = 0; i < rows_; ++i) {
    delete[] matrix_[i];
    matrix_[i] = nullptr;
  }
  delete[] matrix_;
  matrix_ = nullptr;
}

int Matrix::getRows() const noexcept { return rows_; }

int Matrix::getCols() const noexcept { return cols_; }

void Matrix::setRows(const int rows) {
  if (rows < 1)
    throw std::length_error(
        "Invalid input, matrices must have a positive size");
  if (rows != rows_) {
    Matrix tmp(rows, cols_);
    int filling_rows = rows_ < rows ? rows_ : rows;
    for (int i = 0; i < filling_rows; i++) {
      memcpy(tmp.matrix_[i], matrix_[i], cols_ * sizeof(double));
    }
    *this = std::move(tmp);
  }
}

void Matrix::setCols(const int cols) {
  if (cols < 1)
    throw std::length_error(
        "Invalid input, matrices must have a positive size");
  if (cols != cols_) {
    Matrix tmp(rows_, cols);
    int filling_cols = cols_ < cols ? cols_ : cols;
    for (int i = 0; i < rows_; i++) {
      memcpy(tmp.matrix_[i], matrix_[i], filling_cols * sizeof(double));
    }
    *this = std::move(tmp);
  }
}

bool Matrix::EqMatrix(const Matrix &other) const noexcept {
  if (cols_ != other.cols_ || rows_ != other.rows_) {
    return false;
  } else {
    for (int i = 0; i < rows_; i++) {
      for (int j = 0; j < cols_; j++) {
        if (fabs(matrix_[i][j] - other.matrix_[i][j]) > 1e-7) return false;
      }
    }
  }
  return true;
}

void Matrix::SumMatrix(const Matrix &other) {
  if (cols_ != other.cols_ || rows_ != other.rows_)
    throw std::out_of_range("Matrix must be the same size");
  for (int i = 0; i < rows_; i++) {
    for (int j = 0; j < cols_; j++) {
      matrix_[i][j] += other.matrix_[i][j];
    }
  }
}

void Matrix::SubMatrix(const Matrix &other) {
  if (cols_ != other.cols_ || rows_ != other.rows_)
    throw std::out_of_range("Matrix must be the same size");
  for (int i = 0; i < rows_; i++) {
    for (int j = 0; j < cols_; j++) {
      matrix_[i][j] -= other.matrix_[i][j];
    }
  }
}

void Matrix::MulNumber(const double num) noexcept {
  for (int i = 0; i < rows_; i++) {
    for (int j = 0; j < cols_; j++) {
      matrix_[i][j] *= num;
    }
  }
}

void Matrix::MulMatrix(const Matrix &other) {
  if (cols_ != other.rows_)
    throw std::out_of_range(
        "The number of columns of the first matrix is not equal to the "
        "number of rows of the second matrix");
  Matrix tmp(rows_, cols_);
  for (int i = 0; i < rows_; i++) {
    for (int j = 0; j < other.cols_; j++) {
      for (int k = 0; k < rows_; k++) {
        tmp.matrix_[i][j] += matrix_[i][k] * other.matrix_[k][j];
      }
    }
  }
  *this = std::move(tmp);
}

Matrix Matrix::Transpose() const noexcept {
  Matrix result(rows_, cols_);
  for (int i = 0; i < rows_; i++) {
    for (int j = 0; j < cols_; j++) {
      result.matrix_[j][i] = matrix_[i][j];
    }
  }
  return result;
}

double Matrix::Determinant() const {
  if (cols_ != rows_) throw std::logic_error("The matrix is not square");
  if (rows_ == 1) {
    return matrix_[0][0];
  } else if (rows_ == 2) {
    return matrix_[0][0] * matrix_[1][1] - matrix_[0][1] * matrix_[1][0];
  } else {
    double determinant = 0;
    double result = 0;
    for (int i = 0; i < rows_; i++) {
      Matrix minor = this->Minor(1, i + 1);
      determinant = minor.Determinant();
      if (i % 2 == 0) {
        result += matrix_[0][i] * determinant;
      } else {
        result -= matrix_[0][i] * determinant;
      }
    }
    return result;
  }
}

Matrix Matrix::CalcComplements() const {
  double determinant;
  try {
    determinant = this->Determinant();
  } catch (std::logic_error &e) {
    throw e;
  }
  Matrix result(rows_, cols_);
  if (cols_ == 1) {
    determinant = this->Determinant();
    result.matrix_[0][0] = determinant;
  } else {
    for (int i = 0; i < rows_; i++) {
      for (int j = 0; j < cols_; j++) {
        Matrix minor = this->Minor(i + 1, j + 1);
        determinant = minor.Determinant();
        if ((i + j) % 2 == 0) {
          result.matrix_[i][j] = determinant;
        } else {
          result.matrix_[i][j] = -determinant;
        }
      }
    }
  }
  return result;
}

Matrix Matrix::InverseMatrix() const {
  double determinant;
  try {
    determinant = this->Determinant();
  } catch (std::logic_error &e) {
    throw e;
  }
  if (fabs(determinant) < 1e-06)
    throw std::logic_error("Determinant can't be zero");
  Matrix transpose_calc_complements = this->CalcComplements().Transpose();
  transpose_calc_complements.MulNumber(1 / determinant);
  return transpose_calc_complements;
}

Matrix Matrix::Minor(int row, int column) const noexcept {
  Matrix result(rows_ - 1, cols_ - 1);
  for (int i = 0, o = 0; i < rows_; i++) {
    if (i == row - 1) {
      continue;
    }
    for (int j = 0, m = 0; j < cols_; j++) {
      if (j == column - 1) {
        continue;
      }
      result.matrix_[o][m] = matrix_[i][j];
      m++;
    }
    o++;
  }
  return result;
}

void Matrix::AllocateMatrix() {
  matrix_ = new double *[rows_];
  if (!matrix_) {
    rows_ = 0;
    cols_ = 0;
    throw std::bad_alloc();
  }
  for (int i = 0; i < rows_; ++i) {
    matrix_[i] = new double[cols_]{0};
    // Выделение памяти под указатели на строки
    if (!matrix_[i]) {
      // Освобождение памяти, если не удалось выделить
      cols_ = i;
      this->~Matrix();
      throw std::bad_alloc();
    }
  }
}

double &Matrix::operator()(int i, int j) const {
  if (i < 0 || j < 0 || i > rows_ - 1 || j > cols_ - 1)
    throw std::out_of_range("Matrix out of range");
  return matrix_[i][j];
}

Matrix Matrix::operator+(const Matrix &other) const noexcept {
  Matrix tmp(*this);
  tmp.SumMatrix(other);
  return tmp;
}

Matrix Matrix::operator-(const Matrix &other) const noexcept {
  Matrix tmp(*this);
  tmp.SubMatrix(other);
  return tmp;
}

Matrix Matrix::operator*(const Matrix &other) const noexcept {
  Matrix tmp(*this);
  tmp.MulMatrix(other);
  return tmp;
}

Matrix Matrix::operator*(const double num) const noexcept {
  Matrix tmp(*this);
  tmp.MulNumber(num);
  return tmp;
}

bool Matrix::operator==(const Matrix &other) const noexcept {
  return EqMatrix(other);
}

Matrix &Matrix::operator*=(const Matrix &other) noexcept {
  MulMatrix(other);
  return *this;
}

Matrix &Matrix::operator*=(const double num) noexcept {
  MulNumber(num);
  return *this;
}

Matrix &Matrix::operator+=(const Matrix &other) noexcept {
  SumMatrix(other);
  return *this;
}

Matrix &Matrix::operator-=(const Matrix &other) noexcept {
  SubMatrix(other);
  return *this;
}

Matrix &Matrix::operator=(const Matrix &other) {
  if (&other != this) {
    this->~Matrix();
    rows_ = other.rows_;
    cols_ = other.cols_;
    // Выделение памяти для новой матрицы
    try {
      this->AllocateMatrix();
    } catch (std::bad_alloc &e) {
      throw e;
    }
    for (int i = 0; i < rows_; ++i) {
      memcpy(matrix_[i], other.matrix_[i], cols_ * sizeof(double));
    }
  }
  return *this;
}

Matrix &Matrix::operator=(Matrix &&other) noexcept {
  if (this != &other) {
    this->~Matrix();
    matrix_ = other.matrix_;
    rows_ = other.rows_;
    cols_ = other.cols_;
    other.matrix_ = nullptr;
    other.rows_ = 0;
    other.cols_ = 0;
  }
  return *this;
}
