const express = require("express");
const mysql = require("mysql2");

const app = express();
const PORT = 3000;

const connection = mysql.createConnection({
  host: process.env.DB_HOST || "mysql",
  user: process.env.DB_USER || "user",
  password: process.env.DB_PASSWORD || "password",
  database: process.env.DB_NAME || "mydb",
});

app.get("/", (req, res) => {
  connection.query("SELECT NOW() AS current_time", (err, results) => {
    if (err) {
      return res
        .status(500)
        .json({ error: "Database connection failed", detail: err.message });
    }
    res.json({
      message: "Node.js connected to MySQL successfully!",
      mysql_time: results[0].current_time,
    });
  });
});

app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});
