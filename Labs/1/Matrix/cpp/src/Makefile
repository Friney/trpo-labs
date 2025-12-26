CC = g++
CFLAGS = -Wall -Werror -Wextra -std=c++17
LIBS = -lgtest
SOURCE = $(wildcard *.cc)
OBJ = $(patsubst %.cc, %.o, $(SOURCE))
LIB = matrix.a
TEST = ./tests/test
REPORT = report

ifeq ($(shell uname), Darwin)
	OPEN_REPORT = open
endif
ifeq ($(shell uname), Linux)
	OPEN_REPORT = xdg-open
endif

all : $(LIB) $(TEST)

rebuild: clean all

object: $(SOURCE)
	$(CC) $(CFLAGS) -c $(SOURCE) -o $(OBJ)

$(LIB): object
	ar rc $@ $(OBJ)
	ranlib $@

test : $(LIB)
	$(CC) $(TEST).cc $(LIB) -o $(TEST) $(LIBS)
	$(TEST)

clean:
	rm -rf $(TEST) $(LIB) $(OBJ) $(REPORT) $(REPORT).info *.gcda *.gcno gcov_report

test_leaks: test
	valgrind --leak-check=yes $(TEST)

gcov_report: $(TEST)
	mkdir -p $(REPORT)
	$(CC) -fprofile-arcs -ftest-coverage $(TEST).cc $(SOURCE) -o gcov_report $(LIBS) $(CFLAGS)
	./gcov_report
	lcov -t "test" --no-external -o $(REPORT).info -c -d .
	genhtml -o $(REPORT) $(REPORT).info
	$(OPEN_REPORT) $(REPORT)/index.html

.PHONY: all $(LIB) object $(TEST) clang_format clang_edit rebuild test_leaks gcov_report