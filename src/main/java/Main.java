import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {

    public static void main(String[] args) {
        if (args.length > 1) {
            System.err.println("Unexpected arguments: " + Arrays.toString(Arrays.copyOfRange(args, 1, args.length)));
            return;
        } else if (args.length == 0) {
            System.err.println("Usage: Cna source_file");
            return;
        }
        if (!args[0].endsWith(".cna")) {
            System.err.println("Expected '*.cna', got '" + args[0] + "'");
            return;
        }
        Lexer lexer;
        try {
            lexer = new ExprLexer(new ANTLRFileStream(args[0]));
        } catch (FileNotFoundException e) {
            System.err.println("File not found: '" + args[0] + "'");
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprParser parser = new ExprParser(tokens);
        ParseTree tree = parser.prog();
        Interpreter i = new Interpreter();
        i.visit(tree);
    }

}
