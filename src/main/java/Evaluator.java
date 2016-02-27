import java.util.ArrayList;
import java.util.List;

/**
 * Helper class used to simulate dynamic typing. This class cannot be
 * instantiated nor extended.
 */
public final class Evaluator {

	private static final double EPSILON = 0.0000001;

	private Evaluator() {
	}

	/**
	 * Compares two objects using a given operator.
	 * 
	 * @param left
	 *            The object on the left hand side.
	 * @param right
	 *            The object on the right hand side.
	 * @param op
	 *            The operator (e.g. <code>ExprParser.EQ</code>)
	 * @return The result of the comparison.
	 * @throws IllegalArgumentException
	 *             The comparison is invalid.
	 */
	public static boolean evaluateCondition(Object left, Object right, int op) {
		// If left and right are numeric, then they are comparable.
		Double numericLeft = maybeNumeric(left);
		Double numericRight = maybeNumeric(right);
		if (numericLeft != null && numericRight != null) {
			Double l = numericLeft;
			Double r = numericRight;

			switch (op) {
			case ExprParser.LT:
				return l < r;
			case ExprParser.LTE:
				return l <= r;
			case ExprParser.GT:
				return l > r;
			case ExprParser.GTE:
				return l >= r;
			case ExprParser.EQ:
				return Math.abs(l - r) < EPSILON;
			}
		}

		// If either object is non-numeric, then equality becomes trivial.
		else if (op == ExprParser.EQ) {
			if (left instanceof StringBuilder && right instanceof String
					|| left instanceof String && right instanceof StringBuilder) {
				return left.toString().equals(right.toString());
			}
			return left.equals(right);
		}

		// The operation cannot be completed.
		throw new IllegalArgumentException("Invalid comparison between " + left + " and " + right);
	}

	/**
	 * Evaluates an expression by applying the given operator to two objects.
	 * 
	 * @param left
	 *            The object on the left hand side.
	 * @param right
	 *            The object on the right hand side.
	 * @param op
	 *            The operator (e.g. <code>ExprParser.PLUS</code>)
	 * @return The result of the operation.
	 * @throws IllegalArgumentException
	 *             The expression is undefined.
	 */
	public static Object evaluateExpression(Object left, Object right, int op) {
		// Numeric operations will inherit Java rules regarding integer and
		// floating point arithmetic.
		if (left instanceof Integer && right instanceof Integer) {
			Integer l = (Integer) left;
			Integer r = (Integer) right;
			switch (op) {
			case ExprParser.POW:
				return Math.pow(l, r);
			case ExprParser.MOD:
				return l % r;
			case ExprParser.DIVIDE:
				return l / r;
			case ExprParser.TIMES:
				return l * r;
			case ExprParser.PLUS:
				return l + r;
			case ExprParser.MINUS:
				return l - r;
			}
		} else if (left instanceof Double && right instanceof Integer) {
			Double l = (Double) left;
			Integer r = (Integer) right;
			switch (op) {
			case ExprParser.POW:
				return Math.pow(l, r);
			case ExprParser.MOD:
				return l % r;
			case ExprParser.DIVIDE:
				return l / r;
			case ExprParser.TIMES:
				return l * r;
			case ExprParser.PLUS:
				return l + r;
			case ExprParser.MINUS:
				return l - r;
			}
		} else if (left instanceof Integer && right instanceof Double) {
			Integer l = (Integer) left;
			Double r = (Double) right;
			switch (op) {
			case ExprParser.POW:
				return Math.pow(l, r);
			case ExprParser.MOD:
				return l % r;
			case ExprParser.DIVIDE:
				return l / r;
			case ExprParser.TIMES:
				return l * r;
			case ExprParser.PLUS:
				return l + r;
			case ExprParser.MINUS:
				return l - r;
			}
		} else if (left instanceof Double && right instanceof Double) {
			double l = (Double) left;
			double r = (Double) right;
			switch (op) {
			case ExprParser.POW:
				return Math.pow(l, r);
			case ExprParser.MOD:
				return l % r;
			case ExprParser.DIVIDE:
				return l / r;
			case ExprParser.TIMES:
				return l * r;
			case ExprParser.PLUS:
				return l + r;
			case ExprParser.MINUS:
				return l - r;
			}
		}

		// The plus operation is valid for some non-numerical types.
		else if (op == ExprParser.PLUS) {
			if (left instanceof List && right instanceof List) {
				List<Object> list = new ArrayList<Object>();
				list.addAll((List<?>) left);
				list.addAll((List<?>) right);
				return list;
			}
			return left.toString() + right.toString();
		}

		// The operation cannot be completed.
		String message = "Undefined expression. Operands: " + left + "," + right + ". Operator: " + op + ".";
		throw new IllegalArgumentException(message);
	}

	/**
	 * Takes in an object and attempts to convert it to a {@link Double}.
	 * 
	 * @param o
	 *            An object
	 * @return A {@link Double}, or <code>null</code> if the conversion is
	 *         unsuccessful.
	 */
	public static Double maybeNumeric(Object o) {
		Double num = null;
		if (o instanceof Double) {
			num = (Double) o;
		} else if (o instanceof Integer) {
			Integer i = (Integer) o;
			num = (Double.valueOf(i));
		}
		return num;
	}

}
