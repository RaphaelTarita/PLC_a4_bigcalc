import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class BigCalcProgVisitorImpl extends BigCalcProgBaseVisitor<BigDecimal> {
    private final Map<String, BigDecimal> variables = new HashMap<>();

    @Override
    public BigDecimal visitProgram(BigCalcProgParser.ProgramContext ctx) {
        BigDecimal lastResult = BigDecimal.ZERO;
        for (BigCalcProgParser.StatementContext stmt : ctx.statement()) {
            lastResult = visit(stmt);
        }
        return lastResult;
    }

    @Override
    public BigDecimal visitStmtexpr(BigCalcProgParser.StmtexprContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public BigDecimal visitStmtasgn(BigCalcProgParser.StmtasgnContext ctx) {
        return visit(ctx.assignment());
    }

    @Override
    public BigDecimal visitAssignment(BigCalcProgParser.AssignmentContext ctx) {
        BigDecimal result = visit(ctx.expression());
        variables.put(ctx.ID().getText(), result);
        return result;
    }

    @Override
    public BigDecimal visitParentheses(BigCalcProgParser.ParenthesesContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public BigDecimal visitMulDiv(BigCalcProgParser.MulDivContext ctx) {
        BigDecimal lop = visit(ctx.expression(0));
        BigDecimal rop = visit(ctx.expression(1));
        switch (ctx.op.getText()) {
            case "*":
                return lop.multiply(rop);
            case "/":
                return lop.divide(rop, 10, RoundingMode.HALF_UP);
            default:
                throw new AssertionError("unreachable"); // ANTLR should handle unknown operators
        }
    }

    @Override
    public BigDecimal visitAddSub(BigCalcProgParser.AddSubContext ctx) {
        BigDecimal lop = visit(ctx.expression(0));
        BigDecimal rop = visit(ctx.expression(1));
        switch (ctx.op.getText()) {
            case "+":
                return lop.add(rop);
            case "-":
                return lop.subtract(rop);
            default:
                throw new AssertionError("unreachable"); // ANTLR should handle unknown operators
        }
    }

    @Override
    public BigDecimal visitVar(BigCalcProgParser.VarContext ctx) {
        return variables.getOrDefault(ctx.ID().getText(), BigDecimal.ZERO);
    }

    @Override
    public BigDecimal visitNum(BigCalcProgParser.NumContext ctx) {
        return new BigDecimal(ctx.Number().getText());
    }
}
