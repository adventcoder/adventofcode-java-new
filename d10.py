import re
import numpy as np
from pulp import LpProblem, LpMinimize, LpVariable, lpSum, LpInteger

# -------------------------------
# Parse a single problem line
# -------------------------------
def parse_line(line):
    # Extract all "(...)" groups for buttons
    button_groups = re.findall(r"\(([^)]*)\)", line)

    # Extract the "{...}" for J
    J_group = re.search(r"\{([^}]*)\}", line)
    if not J_group:
        raise ValueError("No target {…} found in line.")
    J_str = J_group.group(1).strip()

    # Parse J
    J = np.array([int(x) for x in J_str.split(",")], dtype=int)
    n = len(J)   # number of counters (dimension)

    # Parse button index lists
    buttons = []
    for bg in button_groups:
        bg = bg.strip()
        if bg == "":          # handle empty ()
            idxs = []
        else:
            idxs = [int(x) for x in bg.split(",")]
        # Convert to 0/1 vector (column)
        col = np.zeros(n, dtype=int)
        for i in idxs:
            col[i] = 1
        buttons.append(col)

    # Build A: each button is a column
    if buttons:
        A = np.column_stack(buttons)
    else:
        A = np.zeros((n, 0), dtype=int)

    return A, J


# -------------------------------
# Solve one instance with MILP
# -------------------------------
def solve_button_presses(A_int, J_int):
    n, m = A_int.shape
    prob = LpProblem("MinPresses", LpMinimize)
    a = [LpVariable(f"a{i}", lowBound=0, cat=LpInteger) for i in range(m)]
    
    # Objective
    prob += lpSum(a)
    
    # Constraints
    for i in range(n):
        prob += lpSum(A_int[i,j]*a[j] for j in range(m)) == J_int[i]
    
    prob.solve()
    
    return np.array([int(v.value()) for v in a])

# -------------------------------
# Example: process one line
# -------------------------------
if __name__ == "__main__":
    # lines = []
    # lines.append('[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}')
    # lines.append('[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}')
    # lines.append('[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}')
    with open('inputs/2025/10.txt') as file:
        lines = file.read().splitlines()

    total = 0
    for line in lines:

        A, J = parse_line(line)
        print(line)
        print("A matrix:\n", A)
        print("J:", J)

        sol = solve_button_presses(A, J)
        print("Solution:", sol)
        print("Sum of presses:", sol.sum())

        total += sol.sum()

    print()
    print('Gold:', total)
