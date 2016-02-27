import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.antlr.v4.runtime.tree.ParseTree;

public class Interpreter extends ExprBaseVisitor<Object> {

	private Map<String, Object> variables;

	@Override
	public Object visitBlock(ExprParser.BlockContext ctx) {
		int i = 0;
		for (ParseTree kid : ctx.children) {
			if (kid == ctx.NEWLINE(i)) {
				i++;
			} else {
				this.visit(kid);
			}
		}
		return null;
	}

	@Override
	public Object visitList(ExprParser.ListContext ctx) {
		if (ctx.colon != null) {
			Integer begin = (Integer) this.visit(ctx.begin);
			Integer end = (Integer) this.visit(ctx.end);
			Integer step = (Integer) this.visit(ctx.step);
			List<Object> list = new ArrayList<Object>();
			for (int i = begin; i <= end; i += step) {
				list.add(i);
			}
			return list;
		}

		int i = 0;
		List<Object> list = new ArrayList<Object>();
		for (ParseTree kid : ctx.children) {
			if (kid == ctx.SEPARATOR(i)) {
				i++;
			} else if (kid != ctx.LISTBEGIN() && kid != ctx.LISTEND()) {
				list.add(this.visit(kid));
			}
		}
		return list;
	}

	@Override
	public Object visitProg(ExprParser.ProgContext ctx) {
		variables = new HashMap<String, Object>();
		return super.visitProg(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visitStatement(ExprParser.StatementContext stmt) {
		// The programmer is setting an element of a List or String.
		if (stmt.id != null && stmt.index != null) {
			String id = stmt.id.getText();
			int index = (Integer) this.visit(stmt.index) - 1;
			Object object = variables.get(id);

			// Set an element of a List, expanding if necessary.
			if (object instanceof List) {
				List<Object> list = (List<Object>) object;
				while (index >= list.size()) {
					list.add(null);
				}
				list.set(index, this.visit(stmt.right));
			}

			// Set an element of a String, expanding if necessary.
			if (object instanceof StringBuilder) {
				StringBuilder s = (StringBuilder) object;
				while (index >= s.length()) {
					s.append('\0');
				}
				s.setCharAt(index, (Character) this.visit(stmt.right));
			}

			return this.visit(stmt.right);
		}

		// The programmer is assigning a variable.
		if (stmt.id != null) {
			String id = stmt.id.getText();
			Object result = this.visit(stmt.right);
			variables.put(id, result);
			return result;
		}

		// The programmer is using a keyword.
		if (stmt.keyword != null) {
			switch (stmt.keyword.getType()) {
			case ExprParser.PRINT:
				System.out.println(this.visit(stmt.sub));
				break;
			case ExprParser.WRITE:
				System.out.print(this.visit(stmt.sub));
				break;
			case ExprParser.WHILE:
				while ((Boolean) this.visit(stmt.condition)) {
					this.visit(stmt.loop);
				}
				break;
			case ExprParser.IF:
				boolean condition = (Boolean) this.visit(stmt.condition);
				if (condition) {
					this.visit(stmt.loop);
				} else if (stmt.elsedir != null) {
					this.visit(stmt.elseloop);
				}
				break;
			}
			return null;
		}

		return null;
	}

	@Override
	public Object visitExpr(ExprParser.ExprContext expr) {
		// The programmer is using a keyword.
		if (expr.keyword != null) {
			switch (expr.keyword.getType()) {
			case ExprParser.READINT:
				return new Scanner(System.in).nextInt();
			case ExprParser.READDOUBLE:
				return new Scanner(System.in).nextDouble();
			case ExprParser.READSTRING:
				return new Scanner(System.in).nextLine();
			case ExprParser.RANDOM:
				return Math.random();
			case ExprParser.FLOOR:
				Double d = Evaluator.maybeNumeric(this.visit(expr.right));
				if (d != null) {
					return new Double(Math.floor(d)).intValue();
				}
				throw new IllegalArgumentException("Cannot use floor on " + this.visit(expr.right));
			}
		}

		// The programmer is hard-coding a number.
		if (expr.number != null) {
			String number = expr.number.getText();
			if (number.contains(".")) {
				return Double.parseDouble(expr.number.getText());
			}
			return Integer.parseInt(expr.number.getText());
		}

		// The programmer is hard-coding a character.
		if (expr.character != null) {
			return expr.character.getText().charAt(1);
		}

		// The programmer is referring to an element of a List or String.
		if (expr.id != null && expr.index != null) {
			Object object = variables.get(expr.id.getText());
			if (object instanceof List)
				return ((List<?>) object).get((Integer) this.visit(expr.index) - 1);
			if (object instanceof StringBuilder)
				return ((StringBuilder) object).charAt((Integer) this.visit(expr.index) - 1);
		}

		// The programmer is referring to a variable.
		if (expr.id != null) {
			return variables.get(expr.id.getText());
		}

		// The programmer is parenthesizing an expression.
		if (expr.sub != null) {
			return this.visit(expr.sub);
		}

		// The programmer is performing a mathematical operation.
		if (expr.op != null) {
			return Evaluator.evaluateExpression(this.visit(expr.left), this.visit(expr.right), expr.op.getType());
		}

		// The programmer is hard-coding a list.
		if (expr.list_expr != null) {
			return this.visit(expr.list_expr);
		}

		// The programmer is hard-coding a string.
		if (expr.string != null) {
			String s = expr.string.getText();
			return new StringBuilder(s.substring(1, s.length() - 1));
		}

		// The programmer is using a comparator.
		if (expr.comparator != null) {
			return Evaluator.evaluateCondition(this.visit(expr.left), this.visit(expr.right), expr.comparator.getType());
		}

		// The programmer is using the negation operator.
		if (expr.bang != null) {
			return !((Boolean) this.visit(expr.right));
		}

		// The programmer is using a logical operator.
		if (expr.logicop != null) {
			Boolean left = (Boolean) this.visit(expr.left);
			Boolean right = (Boolean) this.visit(expr.right);

			switch (expr.logicop.getType()) {
			case ExprParser.AND:
				return left && right;
			case ExprParser.OR:
				return left || right;
			default:
				return null;
			}
		}

		return null;
	}

}
