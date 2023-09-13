function calculate(lhs: number, rhs: number, operator: string): number {
    if (operator === '+') {
        return lhs + rhs
    } else if (operator === '-') {
        return lhs - rhs
    } else if (operator === '*') {
        return lhs * rhs
    } else if (operator === '/') {
        return lhs / rhs;
    } else {
        return 0
    }
}

console.log(calculate(10, 5, '+'))
console.log(calculate(10, 5, '-'))
console.log(calculate(10, 5, '*'))
console.log(calculate(10, 5, '/'))