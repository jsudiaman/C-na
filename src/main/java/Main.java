import java.io.IOException;
import java.util.Arrays;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {

	public static void main(String[] args) throws IOException {
		if (args.length > 1) {
			throw new RuntimeException("Unexpected arguments: " + Arrays.toString(Arrays.copyOfRange(args, 1, args.length)));
		} else if (args.length == 0) {
			System.out.println("Usage: Cna source_file");
			return;
		}
		Lexer lexer = new ExprLexer(new ANTLRFileStream(args[0]));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ExprParser parser = new ExprParser(tokens);
		ParseTree tree = parser.prog();
		Interpreter i = new Interpreter();
		i.visit(tree);
	}

}
