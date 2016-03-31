# C-na

## Prerequisite Software
[Java 6 or later](http://java.com/)

## Quick Start
```
curl -O http://sudicode.com/C-na/Cna.jar
echo 'SUPERSLAM "Hello world!"' > HelloWorld.cna
java -jar Cna.jar HelloWorld.cna
```

## Building from Source
```
git clone https://github.com/sudiamanj/C-na.git
cd C-na
./gradlew fatJar
cp build/libs/C-na-all.jar Cna.jar
java -jar Cna.jar sample-programs/FizzBuzz.cna
```

## C-na Syntax
| Keyword             | Definition                                                                 |
|---------------------|----------------------------------------------------------------------------|
| ``SLAM``            | Prints the given expression.                                               |
| ``SUPERSLAM``       | Prints the given expression followed by a NEWLINE.                         |
| ``HUSTLE``          | if (...)                                                                   |
| ``WHAT NOW``        | else                                                                       |
| ``MY TIME IS NOW``  | while (...)                                                                |
| ``YOUR TIME IS UP`` | end if / end while                                                         |
| ``[]``              | Creates an empty list.                                                     |
| ``[a,b,c]``         | Creates a list that contains a, b, and c.                                  |
| ``[min:max:step]``  | Creates a list that counts by ``step`` from ``min`` to ``max``, inclusive. |
| ``YOU CANT SEE ME`` | Prompts the user for an integer.                                           |
| ``THE CHAMP``       | Prompts the user for a double.                                             |
| ``MAKE IT LOUD``    | Prompts the user for a string.                                             |

## Typing
* C-na is dynamically typed. ``a = 1``, ``a = 1.0``, and ``a = "Hello world!"`` are all valid declarations.

## Lists
* Lists are 1-indexed. For example, if ``a = [10,20,30]``, then ``a[1] = 10``.
* Lists are dynamically allocated. For example, if ``a`` is an empty list, then ``a[1] = s`` will append ``s`` to the list.
* The ``+`` operator concatenates lists. If ``a = [1,2,3]`` and ``b = [4,5,6]``, then ``a + b = [1,2,3,4,5,6]``.

## Arithmetic
| Operator                 | Definition                                                  |
|--------------------------|-------------------------------------------------------------|
| ``+``                    | Adds two expressions.                                       |
| ``-``                    | Subtracts two expressions.                                  |
| ``*``                    | Multiplies two expressions.                                 |
| ``/``                    | Divides two expressions. (Beware of integer division.)      |
| ``%``                    | Modulo operator.                                            |
| ``^``                    | Power operator.                                             |
| ``THE UNDERTAKER``       | Converts the given expression to an integer, rounding down. |
| ``FIVE KNUCKLE SHUFFLE`` | A random double between 0 and 1.                            |

## FizzBuzz.cna
```
i = 1
arr = []

SLAM "Enter the max: "
MAX = YOU CANT SEE ME
MY TIME IS NOW (i <= MAX)
	divBy3 = (i % 3) == 0
	divBy5 = (i % 5) == 0

	HUSTLE (divBy3 && divBy5)
		arr[i] = "Fizz Buzz"
	WHAT NOW
		HUSTLE (divBy3)
			arr[i] = "Fizz"
		WHAT NOW
			HUSTLE (divBy5)
				arr[i] = "Buzz"
			WHAT NOW
				arr[i] = i
			YOUR TIME IS UP
		YOUR TIME IS UP
	YOUR TIME IS UP

	i = (i+1)
YOUR TIME IS UP

SUPERSLAM arr
```
