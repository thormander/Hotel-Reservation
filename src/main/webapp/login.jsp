<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Guest Login Form</title>

  <!-- Bootstrap CSS -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.1.0/css/bootstrap.min.css">
</head>
<body class="bg-light">

  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container">
      <a class="navbar-brand" href="#">Guest Login</a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
        aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav">
          <li class="nav-item">
            <a class="nav-link" href="loginAdmin.jsp">Login as Admin</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="loginClerk.jsp">Login as Clerk</a>
          </li>
        </ul>
      </div>
    </div>
  </nav>

  <div class="container">
    <div class="row">
      <div class="col-lg-4 col-md-6 col-sm-12">
        <!-- Sign in Form -->
        <div class="card p-4 shadow-sm mt-5">
          <h2 class="text-center mb-4">Guest Sign in</h2>
          <form method="post" action="accountHandler">
            <input type="hidden" name="accountType" value="guest">
            <div class="mb-3">
              <label for="username" class="form-label">Your Email</label>
              <input type="text" name="username" id="username" class="form-control" required>
            </div>
            <div class="mb-3">
              <label for="password" class="form-label">Password</label>
              <input type="password" name="password" id="password" class="form-control" required>
            </div>
            <button type="submit" name="signin" id="signin" class="btn btn-primary w-100">Log in</button>
          </form>
          <a class="d-block text-center text-decoration-none mt-3 text-muted" href="registration.jsp">Create a new guest account</a>
        </div>
      </div>
    </div>
  </div>

  <!-- Bootstrap JS -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.1.0/js/bootstrap.bundle.min.js"></script>
</body>
</html>
