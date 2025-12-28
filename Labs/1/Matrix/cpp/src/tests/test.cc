#include <gtest/gtest.h>

#include "../matrix.h"

TEST(TestGroupMatrix, wrong_constructor) {
  EXPECT_ANY_THROW(Matrix matrix(-7, -1));
}

TEST(TestGroupMatrix, copy_constructor) {
  double values[3][3] = {
      {2, 5, 7},
      {6, 3, 4},
      {5, -2, -3},
  };
  Matrix matrix(3, 3);
  for (int i = 0; i < matrix.getRows(); ++i) {
    for (int j = 0; j < matrix.getCols(); ++j) {
      matrix(i, j) = values[i][j];
    }
  }
  Matrix matrix_copy(matrix);
  EXPECT_TRUE(matrix == matrix_copy);
  EXPECT_EQ(matrix.getCols(), 3);
  EXPECT_EQ(matrix.getRows(), 3);
}

TEST(TestGroupMatrix, move_constructor) {
  double values[3][3] = {
      {2, 5, 7},
      {6, 3, 4},
      {5, -2, -3},
  };
  Matrix matrix(3, 3);
  for (int i = 0; i < matrix.getRows(); ++i) {
    for (int j = 0; j < matrix.getCols(); ++j) {
      matrix(i, j) = values[i][j];
    }
  }
  Matrix matrix_copy(std::move(matrix));
  EXPECT_EQ(matrix.getCols(), 0);
  EXPECT_EQ(matrix.getRows(), 0);
}

TEST(TestGroupMatrix, scobs_operator) {
  Matrix matrix(3, 3);
  EXPECT_ANY_THROW(matrix(8, 8));
}

TEST(TestGroupMatrix, setRows_up) {
  double values[3][3] = {
      {2, 5, 7},
      {6, 3, 4},
      {5, -2, -3},
  };
  Matrix matrix(3, 3);
  for (int i = 0; i < matrix.getRows(); ++i) {
    for (int j = 0; j < matrix.getCols(); ++j) {
      matrix(i, j) = values[i][j];
    }
  }
  matrix.setRows(5);
  EXPECT_EQ(matrix.getRows(), 5);
  EXPECT_EQ(matrix(4, 0), 0);
}

TEST(TestGroupMatrix, setCols_up) {
  double values[3][3] = {
      {2, 5, 7},
      {6, 3, 4},
      {5, -2, -3},
  };
  Matrix matrix(3, 3);
  for (int i = 0; i < matrix.getRows(); ++i) {
    for (int j = 0; j < matrix.getCols(); ++j) {
      matrix(i, j) = values[i][j];
    }
  }
  matrix.setCols(5);
  EXPECT_EQ(matrix.getCols(), 5);
  EXPECT_EQ(matrix(0, 4), 0);
}

TEST(TestGroupMatrix, setCols_down) {
  double values[3][3] = {
      {2, 5, 7},
      {6, 3, 4},
      {5, -2, -3},
  };
  Matrix matrix(3, 3);
  for (int i = 0; i < matrix.getRows(); ++i) {
    for (int j = 0; j < matrix.getCols(); ++j) {
      matrix(i, j) = values[i][j];
    }
  }
  EXPECT_ANY_THROW(matrix.setCols(-1));
  EXPECT_EQ(matrix.getCols(), 3);
}

TEST(TestGroupMatrix, setRows_down) {
  double values[3][3] = {
      {2, 5, 7},
      {6, 3, 4},
      {5, -2, -3},
  };
  Matrix matrix(3, 3);
  for (int i = 0; i < matrix.getRows(); ++i) {
    for (int j = 0; j < matrix.getCols(); ++j) {
      matrix(i, j) = values[i][j];
    }
  }
  EXPECT_ANY_THROW(matrix.setRows(-1));
  EXPECT_EQ(matrix.getRows(), 3);
}

TEST(TestGroupMatrix, equal_lvalue) {
  Matrix matrix(2, 2);
  Matrix matrix_copy;

  matrix_copy = matrix;

  EXPECT_EQ(matrix_copy.getCols(), 2);
  EXPECT_EQ(matrix_copy.getRows(), 2);
  EXPECT_EQ(matrix.getCols(), 2);
  EXPECT_EQ(matrix.getRows(), 2);
}

TEST(TestGroupMatrix, equal_rvalue) {
  Matrix matrix(2, 2);
  Matrix matrix_moved;

  matrix_moved = std::move(matrix);

  EXPECT_EQ(matrix_moved.getCols(), 2);
  EXPECT_EQ(matrix_moved.getRows(), 2);
}

TEST(TestGroupMatrix, determinant) {
  double values[3][3] = {
      {2, 5, 7},
      {6, 3, 4},
      {5, -2, -3},
  };
  Matrix matrix(3, 3);
  for (int i = 0; i < matrix.getRows(); ++i) {
    for (int j = 0; j < matrix.getCols(); ++j) {
      matrix(i, j) = values[i][j];
    }
  }
  EXPECT_EQ(matrix.Determinant(), -1);
  Matrix tmp(1, 7);
  EXPECT_ANY_THROW(tmp.Determinant());
}

TEST(test_overload, transpose) {
  double values[3][3] = {
      {2, 5, 7},
      {6, 3, 4},
      {5, -2, -3},
  };
  Matrix matrix(3, 3);
  for (int i = 0; i < matrix.getRows(); ++i) {
    for (int j = 0; j < matrix.getCols(); ++j) {
      matrix(i, j) = values[i][j];
    }
  }
  Matrix matrix_transpose = matrix.Transpose().Transpose();
  EXPECT_TRUE(matrix == matrix_transpose);
}

TEST(test_overload, calc_complements) {
  double values[3][3] = {
      {2, 5, 7},
      {6, 3, 4},
      {5, -2, -3},
  };
  Matrix matrix(3, 3);
  for (int i = 0; i < matrix.getRows(); ++i) {
    for (int j = 0; j < matrix.getCols(); ++j) {
      matrix(i, j) = values[i][j];
    }
  }
  Matrix matrix_calc_complements = matrix.CalcComplements();
  double values_test[3][3] = {
      {-1, 38, -27},
      {1, -41, 29},
      {-1, 34, -24},
  };
  for (int i = 0; i < matrix.getRows(); ++i) {
    for (int j = 0; j < matrix.getCols(); ++j) {
      matrix(i, j) = values_test[i][j];
    }
  }
  EXPECT_TRUE(matrix == matrix_calc_complements);
  Matrix matrix_1v1(1, 1);
  matrix_1v1(0, 0) = 5;
  matrix_calc_complements = matrix_1v1.CalcComplements();
  EXPECT_EQ(matrix_calc_complements(0, 0), 5);
  Matrix tmp(1, 7);
  EXPECT_ANY_THROW(tmp.CalcComplements());
}

TEST(test_overload, inverse_matrix) {
  double values[3][3] = {
      {2, 5, 7},
      {6, 3, 4},
      {5, -2, -3},
  };
  Matrix matrix(3, 3);
  for (int i = 0; i < matrix.getRows(); ++i) {
    for (int j = 0; j < matrix.getCols(); ++j) {
      matrix(i, j) = values[i][j];
    }
  }
  Matrix inverse_matrix = matrix.InverseMatrix();
  double values_test[3][3] = {
      {1, -1, 1},
      {-38, 41, -34},
      {27, -29, 24},
  };
  for (int i = 0; i < matrix.getRows(); ++i) {
    for (int j = 0; j < matrix.getCols(); ++j) {
      matrix(i, j) = values_test[i][j];
    }
  }
  EXPECT_TRUE(matrix == inverse_matrix);
  Matrix tmp(1, 7);
  EXPECT_ANY_THROW(tmp.InverseMatrix());
}

TEST(test_overload, mul_matrix) {
  double values[3][3] = {
      {2, 5, 7},
      {6, 3, 4},
      {5, -2, -3},
  };
  Matrix matrix_1(3, 3);
  for (int i = 0; i < matrix_1.getRows(); ++i) {
    for (int j = 0; j < matrix_1.getCols(); ++j) {
      matrix_1(i, j) = values[i][j];
    }
  }
  Matrix matrix_2(3, 3);
  Matrix matrix_copy(matrix_1);
  double values_2[3][3] = {
      {1, 0, 0},
      {0, 1, 0},
      {0, 0, 1},
  };
  for (int i = 0; i < matrix_2.getRows(); ++i) {
    for (int j = 0; j < matrix_2.getCols(); ++j) {
      matrix_2(i, j) = values_2[i][j];
    }
  }
  matrix_1 = matrix_1 * matrix_2;
  EXPECT_TRUE(matrix_1 == matrix_copy);
  Matrix tmp(1, 7);
  EXPECT_ANY_THROW(tmp.MulMatrix(matrix_1));
}

TEST(test_overload, mul_eq_matrix) {
  double values[3][3] = {
      {2, 5, 7},
      {6, 3, 4},
      {5, -2, -3},
  };
  Matrix matrix_1(3, 3);
  for (int i = 0; i < matrix_1.getRows(); ++i) {
    for (int j = 0; j < matrix_1.getCols(); ++j) {
      matrix_1(i, j) = values[i][j];
    }
  }
  Matrix matrix_2(3, 3);
  Matrix matrix_copy(matrix_1);
  double values_2[3][3] = {
      {1, 0, 0},
      {0, 1, 0},
      {0, 0, 1},
  };
  for (int i = 0; i < matrix_2.getRows(); ++i) {
    for (int j = 0; j < matrix_2.getCols(); ++j) {
      matrix_2(i, j) = values_2[i][j];
    }
  }
  matrix_1 *= matrix_2;
  EXPECT_TRUE(matrix_1 == matrix_copy);
}

TEST(test_overload, mul_number) {
  double values[3][3] = {
      {2, 5, 7},
      {6, 3, 4},
      {5, -2, -3},
  };
  Matrix matrix_1(3, 3);
  for (int i = 0; i < matrix_1.getRows(); ++i) {
    for (int j = 0; j < matrix_1.getCols(); ++j) {
      matrix_1(i, j) = values[i][j];
    }
  }
  Matrix matrix_2(3, 3);
  double values_2[3][3] = {
      {4, 10, 14},
      {12, 6, 8},
      {10, -4, -6},
  };
  for (int i = 0; i < matrix_2.getRows(); ++i) {
    for (int j = 0; j < matrix_2.getCols(); ++j) {
      matrix_2(i, j) = values_2[i][j];
    }
  }
  matrix_1 = matrix_1 * 2;
  EXPECT_TRUE(matrix_1 == matrix_2);
}

TEST(test_overload, mul_eq_number) {
  double values[3][3] = {
      {2, 5, 7},
      {6, 3, 4},
      {5, -2, -3},
  };
  Matrix matrix_1(3, 3);
  for (int i = 0; i < matrix_1.getRows(); ++i) {
    for (int j = 0; j < matrix_1.getCols(); ++j) {
      matrix_1(i, j) = values[i][j];
    }
  }
  Matrix matrix_2(3, 3);
  double values_2[3][3] = {
      {4, 10, 14},
      {12, 6, 8},
      {10, -4, -6},
  };
  for (int i = 0; i < matrix_2.getRows(); ++i) {
    for (int j = 0; j < matrix_2.getCols(); ++j) {
      matrix_2(i, j) = values_2[i][j];
    }
  }
  matrix_1 *= 2;
  EXPECT_TRUE(matrix_1 == matrix_2);
}

TEST(test_overload, sub_matrix) {
  double values[3][3] = {
      {2, 5, 7},
      {6, 3, 4},
      {5, -2, -3},
  };
  Matrix matrix_1(3, 3);
  for (int i = 0; i < matrix_1.getRows(); ++i) {
    for (int j = 0; j < matrix_1.getCols(); ++j) {
      matrix_1(i, j) = values[i][j];
    }
  }
  Matrix matrix_2(3, 3);
  double values_2[3][3] = {
      {1, 0, 0},
      {0, 1, 0},
      {0, 0, 1},
  };
  for (int i = 0; i < matrix_2.getRows(); ++i) {
    for (int j = 0; j < matrix_2.getCols(); ++j) {
      matrix_2(i, j) = values_2[i][j];
    }
  }
  Matrix matrix_answer(3, 3);
  double values_answer[3][3] = {
      {1, 5, 7},
      {6, 2, 4},
      {5, -2, -4},
  };
  for (int i = 0; i < matrix_answer.getRows(); ++i) {
    for (int j = 0; j < matrix_answer.getCols(); ++j) {
      matrix_answer(i, j) = values_answer[i][j];
    }
  }
  matrix_1 -= matrix_2;
  EXPECT_TRUE(matrix_1 == matrix_answer);
  Matrix tmp(1, 7);
  EXPECT_ANY_THROW(tmp.SubMatrix(matrix_1));
}

TEST(test_overload, sub_eq_matrix) {
  double values[3][3] = {
      {2, 5, 7},
      {6, 3, 4},
      {5, -2, -3},
  };
  Matrix matrix_1(3, 3);
  for (int i = 0; i < matrix_1.getRows(); ++i) {
    for (int j = 0; j < matrix_1.getCols(); ++j) {
      matrix_1(i, j) = values[i][j];
    }
  }
  Matrix matrix_2(3, 3);
  double values_2[3][3] = {
      {1, 0, 0},
      {0, 1, 0},
      {0, 0, 1},
  };
  for (int i = 0; i < matrix_2.getRows(); ++i) {
    for (int j = 0; j < matrix_2.getCols(); ++j) {
      matrix_2(i, j) = values_2[i][j];
    }
  }
  Matrix matrix_answer(3, 3);
  double values_answer[3][3] = {
      {1, 5, 7},
      {6, 2, 4},
      {5, -2, -4},
  };
  for (int i = 0; i < matrix_answer.getRows(); ++i) {
    for (int j = 0; j < matrix_answer.getCols(); ++j) {
      matrix_answer(i, j) = values_answer[i][j];
    }
  }
  matrix_1 = matrix_1 - matrix_2;
  EXPECT_TRUE(matrix_1 == matrix_answer);
}

TEST(test_overload, sum_matrix) {
  double values[3][3] = {
      {2, 5, 7},
      {6, 3, 4},
      {5, -2, -3},
  };
  Matrix matrix_1(3, 3);
  for (int i = 0; i < matrix_1.getRows(); ++i) {
    for (int j = 0; j < matrix_1.getCols(); ++j) {
      matrix_1(i, j) = values[i][j];
    }
  }
  Matrix matrix_2(3, 3);
  double values_2[3][3] = {
      {1, 0, 0},
      {0, 1, 0},
      {0, 0, 1},
  };
  for (int i = 0; i < matrix_2.getRows(); ++i) {
    for (int j = 0; j < matrix_2.getCols(); ++j) {
      matrix_2(i, j) = values_2[i][j];
    }
  }
  Matrix matrix_answer(3, 3);
  double values_answer[3][3] = {
      {3, 5, 7},
      {6, 4, 4},
      {5, -2, -2},
  };
  for (int i = 0; i < matrix_answer.getRows(); ++i) {
    for (int j = 0; j < matrix_answer.getCols(); ++j) {
      matrix_answer(i, j) = values_answer[i][j];
    }
  }
  matrix_1 += matrix_2;
  EXPECT_TRUE(matrix_1 == matrix_answer);
  Matrix tmp(1, 7);
  EXPECT_ANY_THROW(tmp.SumMatrix(matrix_1));
}

TEST(test_overload, sum_eq_matrix) {
  double values[3][3] = {
      {2, 5, 7},
      {6, 3, 4},
      {5, -2, -3},
  };
  Matrix matrix_1(3, 3);
  for (int i = 0; i < matrix_1.getRows(); ++i) {
    for (int j = 0; j < matrix_1.getCols(); ++j) {
      matrix_1(i, j) = values[i][j];
    }
  }
  Matrix matrix_2(3, 3);
  double values_2[3][3] = {
      {1, 0, 0},
      {0, 1, 0},
      {0, 0, 1},
  };
  for (int i = 0; i < matrix_2.getRows(); ++i) {
    for (int j = 0; j < matrix_2.getCols(); ++j) {
      matrix_2(i, j) = values_2[i][j];
    }
  }
  Matrix matrix_answer(3, 3);
  double values_answer[3][3] = {
      {3, 5, 7},
      {6, 4, 4},
      {5, -2, -2},
  };
  for (int i = 0; i < matrix_answer.getRows(); ++i) {
    for (int j = 0; j < matrix_answer.getCols(); ++j) {
      matrix_answer(i, j) = values_answer[i][j];
    }
  }
  matrix_1 = matrix_1 + matrix_2;
  EXPECT_TRUE(matrix_1 == matrix_answer);
}

int main(int argc, char **argv) {
  ::testing::InitGoogleTest(&argc, argv);

  return RUN_ALL_TESTS();
}