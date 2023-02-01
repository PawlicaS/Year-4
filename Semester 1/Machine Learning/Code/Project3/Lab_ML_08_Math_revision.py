import numpy as np

def part1():
    A = np.array([[1., 2.], [3., 4.], [5., 6.]])
    print("1-A, A=\n", A)
    print("1-A, A.T=\n", A.T)
    print()

    a = np.array([1., 2., 3.])
    b = np.array([4., 5., 6.])
    print("1-B, a=", a)
    print("1-B, b=", b)
    # print("1-B, a.T*b=", np.dot(a,b))
    print("1-B, a.T*b=", a.T @ b)
    print("1-B, a*b.T=\n", np.outer(a, b))
    print()

    A = np.array([[1., 2.], [3., 4.], [5., 6.]])
    b = np.array([7., 8.])
    print("1-C, A=\n", A)
    print("1-C, b=\n", b)
    # print("1-C, Ab=\n", np.matmul(A,b))
    print("1-C, Ab=\n", A @ b)
    print()

    A = np.array([[1., 2.], [3., 4.], [5., 6.]])
    B = np.array([[1., 2., 3., 4.], [5., 6., 7., 8.]])
    print("1-D, A=\n", A)
    print("1-D, B=\n", B)
    # print("1-D, AB=\n", np.matmul(A,B))
    print("1-D, AB=\n", A @ B)
    print()

    # print("1-E, B.T*A.T=\n", np.matmul(B.T,A.T))
    print("1-E, B.T*A.T=\n", B.T @ A.T)
    print()
    return


def part2():
    A = np.array([[1., 2.], [3., 4.], [5., 6.]])
    AtA = A.T @ A
    AtA_inv = np.linalg.inv(AtA)
    print("2-A, A=\n", A)
    print("2-A, AtA=\n", AtA)
    print("2-A, AtA^-1=\n", AtA_inv)
    AAt = A @ A.T
    print("2-A, AAt=\n", AAt)
    try:
        print("2-A, AAt^-1=\n", np.linalg.inv(AAt))
    except:
        print("singular")
    print()

    x = np.array([1., 2.])
    b = AtA @ x
    print("2-B x=", x)
    print("2-B b=", b)
    print("2-B AtA_inv*b=", np.matmul(AtA_inv, b))
    print()
    return


def part3():
    def num_coefficients_3(d):
        t = 0
        for n in range(d + 1):
            for i in range(n + 1):
                for j in range(n + 1):
                    for k in range(n + 1):
                        if i + j + k == n:
                            t = t + 1
        return t

    def eval_poly_3(d, a, x):
        r = 0
        t = 0
        for n in range(d + 1):
            for i in range(n + 1):
                for j in range(n + 1):
                    for k in range(n + 1):
                        if i + j + k == n:
                            r += a[t] * (x[0] ** i) * (x[1] ** j) * (x[2] ** k)
                            t = t + 1
        return r

    d = 2
    k = num_coefficients_3(d)
    print("3-A k=", k)
    print()

    a = np.array([1., 2., 3., 4., 5., 6., 7., 8., 9., 10.])
    print("3-B a=", a)
    x = np.array([2., 3., 4.])
    print("3-B x=", x)
    p = eval_poly_3(d, a, x)
    print("3-B p_a[x]=", p)

    print()

    epsilon = 1e-6
    for i in range(3):
        x[i] += epsilon
        p_e = eval_poly_3(d, a, x)
        x[i] -= epsilon
        print("3-C ","i =",i,", p_a[x+e] = ", p_e)
        dp = (p_e - p)/epsilon
        print("3-C dp=", dp)

    return


part1()
part2()
part3()
