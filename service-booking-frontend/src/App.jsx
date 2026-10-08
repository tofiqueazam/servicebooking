import { useState } from "react";
import "./index.css";
import { loginUser } from "./api/auth";

function App() {
  const [page, setPage] = useState("home");
  const [isLogin, setIsLogin] = useState(true);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const services = [
    { icon: "⚡", name: "Electrician", price: "₹199 se" },
    { icon: "🔧", name: "Plumber", price: "₹149 se" },
    { icon: "🧹", name: "Home Cleaning", price: "₹499 se" },
    { icon: "❄️", name: "AC Repair", price: "₹299 se" },
    { icon: "🎨", name: "Painter", price: "₹999 se" },
    { icon: "🪚", name: "Carpenter", price: "₹249 se" },
  ];

  async function handleAuth(e) {
    e.preventDefault();

    try {
      if (isLogin) {
        const data = await loginUser({ email, password });

        console.log("Login response:", data);
        alert("Login successful!");
      } else {
        alert("Register API next step mein connect karenge.");
      }
    } catch (error) {
      alert(error.message);
    }
  }
    
  return (
    <div className="app">
      <header className="navbar">
        <button className="logo" onClick={() => setPage("home")}>
          Service<span>Hub.</span>
        </button>

        <nav>
          <button onClick={() => setPage("home")}>Home</button>
          <a href="#services" onClick={() => setPage("home")}>Services</a>
        </nav>

        <div className="nav-buttons">
          <button
            className="login-btn"
            onClick={() => {
              setIsLogin(true);
              setPage("auth");
            }}
          >
            Login
          </button>

          <button
            className="signup-btn"
            onClick={() => {
              setIsLogin(false);
              setPage("auth");
            }}
          >
            Sign Up
          </button>
        </div>
      </header>

      {page === "home" ? (
        <main>
          <section className="hero" id="home">
            <div className="hero-content">
              <span className="tag">YOUR TRUSTED LOCAL SERVICE PARTNER</span>
              <h1>Your home,<br /><span>in good hands.</span></h1>
              <p>
                Find trusted local professionals for your home.
                Simple booking, transparent prices and reliable service.
              </p>

              <button
                className="primary-button"
                onClick={() =>
                  document.getElementById("services").scrollIntoView({
                    behavior: "smooth",
                  })
                }
              >
                Explore Services →
              </button>
            </div>

            <div className="hero-visual">
              <div className="visual-circle">🏠</div>
            </div>
          </section>

          <section className="services-section" id="services">
            <span className="tag">OUR SERVICES</span>
            <h2>What can we help with?</h2>

            <div className="service-grid">
              {services.map((service) => (
                <article className="service-card" key={service.name}>
                  <div className="service-icon">{service.icon}</div>
                  <h3>{service.name}</h3>
                  <p>Trusted professionals for your needs.</p>

                  <div className="service-footer">
                    <strong>{service.price}</strong>
                    <button
                      onClick={() => {
                        setIsLogin(true);
                        setPage("auth");
                      }}
                    >
                      Book Now →
                    </button>
                  </div>
                </article>
              ))}
            </div>
          </section>
        </main>
      ) : (
        <main className="auth-page">
          <section className="auth-card">
            <div className="auth-icon">🏠</div>

            <span className="tag">WELCOME TO SERVICEHUB</span>

            <h1>{isLogin ? "Welcome back!" : "Create your account"}</h1>

            <p className="auth-description">
              {isLogin
                ? "Login to manage your bookings."
                : "Join us to book trusted local professionals."}
            </p>

            <div className="auth-tabs">
              <button
                className={isLogin ? "active" : ""}
                onClick={() => setIsLogin(true)}
              >
                Login
              </button>

              <button
                className={!isLogin ? "active" : ""}
                onClick={() => setIsLogin(false)}
              >
                Register
              </button>
            </div>

            <form onSubmit={handleAuth}>
              {!isLogin && (
                <label>
                  Full Name
                  <input
                    type="text"
                    placeholder="Enter your full name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                  />
                </label>
              )}

              <label>
                Email Address
                <input
                  type="email"
                  placeholder="you@example.com"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
              </label>

              <label>
                Password
                <input
                  type="password"
                  placeholder="Enter your password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </label>

              <button className="auth-submit" type="submit">
                {isLogin ? "Login" : "Register"}
              </button>
            </form>

            <p className="auth-switch">
              {isLogin ? "New to ServiceHub?" : "Already have an account?"}
              <button onClick={() => setIsLogin(!isLogin)}>
                {isLogin ? " Create account" : " Login"}
              </button>
            </p>

            <button className="back-home" onClick={() => setPage("home")}>
              ← Back to Home
            </button>
          </section>
        </main>
      )}

      <footer>
        <div className="logo">Service<span>Hub.</span></div>
        <p>Making everyday home services simpler.</p>
        <small>© 2026 ServiceHub — Student Project</small>
      </footer>
    </div>
  );
}

export default App;
