import { useState, useEffect } from "react";
import "bootstrap/dist/css/bootstrap.min.css";

// Minimal + professional — full width + colors + working GET BY ID
export default function OrderApp() {
  const BASE_URL = "http://localhost:8080/api/orders";

  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(false);
  const [msg, setMsg] = useState("");

  const [form, setForm] = useState({ email: "", product: "", amount: "" });
  const [findId, setFindId] = useState("");

  const [single, setSingle] = useState(null);   // 👈 NEW (Get by ID result)

  const [update, setUpdate] = useState({
    id: "",
    email: "",
    product: "",
    amount: "",
  });

  const [actionId, setActionId] = useState("");

  const notify = (m) => {
    setMsg(m);
    setTimeout(() => setMsg(""), 2200);
  };

  const load = async () => {
    const res = await fetch(BASE_URL);
    setOrders(await res.json());
  };

  const createOrder = async () => {
    if (!form.email || !form.product || !form.amount)
      return notify("Fill all fields");

    setLoading(true);
    try {
      const res = await fetch(BASE_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          userEmail: form.email,
          productName: form.product,
          amount: form.amount,
        }),
      });

      notify(await res.text());
      setForm({ email: "", product: "", amount: "" });
      load();
    } finally {
      setLoading(false);
    }
  };

  // 👇 FULL FIX (Get by ID)
  const getById = async () => {
    if (!findId) return;

    setSingle(null);

    const res = await fetch(`${BASE_URL}/${findId}`);

    if (!res.ok) {
      notify("Order not found");
      return;
    }

    const data = await res.json();
    setSingle(data);
  };

  const updateOrder = async () => {
    if (!update.id) return notify("Order ID required");

    const res = await fetch(`${BASE_URL}/${update.id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        userEmail: update.email,
        productName: update.product,
        amount: update.amount,
      }),
    });

    notify(await res.text());
    load();
  };

  const doAction = async (path, method = "POST") => {
    if (!actionId) return notify("Order ID required");

    const res = await fetch(`${BASE_URL}/${actionId}/${path}`, { method });
    notify(await res.text());
    load();
  };

  const del = async () => {
    if (!actionId) return notify("Order ID required");

    const res = await fetch(`${BASE_URL}/${actionId}`, {
      method: "DELETE",
    });

    notify(res.ok ? "Deleted" : "Delete failed");
    load();
  };

  useEffect(() => {
    load();
  }, []);

  return (
    <div
      style={{ background: "linear-gradient(135deg,#eef2ff,#f9fafb)" }}
      className="min-vh-100"
    >
      {/* HEADER */}
      <header
        className="py-3 mb-4 shadow-sm"
        style={{
          background: "linear-gradient(135deg,#2563eb,#4f46e5)",
          color: "#fff",
        }}
      >
        <div className="container-fluid d-flex justify-content-between align-items-center">
          <h4 className="m-0 fw-semibold">Order Panel</h4>
          {loading && <span className="small opacity-75">Processing...</span>}
        </div>
      </header>

      <div className="container-fluid pb-5 px-4">
        {msg && <div className="alert alert-primary py-2 shadow-sm">{msg}</div>}

        {/* CREATE */}
        <div className="card mb-4 border-0 shadow-sm" style={{ borderRadius: 14 }}>
          <div className="card-header fw-semibold" style={{ background: "#f3f4ff" }}>
            Create Order
          </div>

          <div className="card-body row g-2">
            <div className="col-md-4">
              <input
                className="form-control"
                placeholder="Email"
                value={form.email}
                onChange={(e) => setForm({ ...form, email: e.target.value })}
              />
            </div>

            <div className="col-md-4">
              <input
                className="form-control"
                placeholder="Product"
                value={form.product}
                onChange={(e) => setForm({ ...form, product: e.target.value })}
              />
            </div>

            <div className="col-md-4">
              <input
                className="form-control"
                type="number"
                placeholder="Amount"
                value={form.amount}
                onChange={(e) => setForm({ ...form, amount: e.target.value })}
              />
            </div>

            <div className="col-12 mt-2">
              <button
                className="btn btn-primary"
                style={{ borderRadius: 10 }}
                onClick={createOrder}
                disabled={loading}
              >
                Create
              </button>
            </div>
          </div>
        </div>

        {/* LIST */}
        <div className="card mb-4 border-0 shadow-sm" style={{ borderRadius: 14 }}>
          <div
            className="card-header d-flex justify-content-between align-items-center fw-semibold"
            style={{ background: "#f7f7fb" }}
          >
            <span>Orders</span>
            <button
              className="btn btn-sm btn-outline-primary"
              style={{ borderRadius: 10 }}
              onClick={load}
            >
              Reload
            </button>
          </div>

          <div className="card-body p-0">
            <table className="table table-sm m-0 align-middle table-hover">
              <thead className="table-light">
                <tr>
                  <th>ID</th>
                  <th>Email</th>
                  <th>Product</th>
                  <th>Amount</th>
                  <th>Status</th>
                </tr>
              </thead>

              <tbody>
                {orders.map((o) => (
                  <tr key={o.id}>
                    <td>{o.id}</td>
                    <td>{o.userEmail}</td>
                    <td>{o.productName}</td>
                    <td>{o.amount}</td>
                    <td>
                      <span className="badge bg-info text-dark">{o.status}</span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        {/* FIND */}
        <div className="card mb-4 border-0 shadow-sm" style={{ borderRadius: 14 }}>
          <div className="card-header fw-semibold" style={{ background: "#f3f4ff" }}>
            Get By ID
          </div>

          <div className="card-body">
            <div className="d-flex gap-2">
              <input
                className="form-control"
                placeholder="Order ID"
                value={findId}
                onChange={(e) => setFindId(e.target.value)}
              />

              <button
                className="btn btn-secondary"
                style={{ borderRadius: 10 }}
                onClick={getById}
              >
                Find
              </button>
            </div>

            {/* 👇 Show result here */}
            {single && (
              <div className="mt-3 p-3 border rounded bg-light small">
                <div>
                  <b>ID:</b> {single.id}
                </div>
                <div>
                  <b>Email:</b> {single.userEmail}
                </div>
                <div>
                  <b>Product:</b> {single.productName}
                </div>
                <div>
                  <b>Amount:</b> {single.amount}
                </div>
                <div>
                  <b>Status:</b> {single.status}
                </div>
              </div>
            )}
          </div>
        </div>

        {/* UPDATE */}
        <div className="card mb-4 border-0 shadow-sm" style={{ borderRadius: 14 }}>
          <div className="card-header fw-semibold" style={{ background: "#f7f7fb" }}>
            Update
          </div>

          <div className="card-body row g-2">
            <div className="col-md-3">
              <input
                className="form-control"
                placeholder="Order ID"
                value={update.id}
                onChange={(e) => setUpdate({ ...update, id: e.target.value })}
              />
            </div>

            <div className="col-md-3">
              <input
                className="form-control"
                placeholder="Email"
                value={update.email}
                onChange={(e) => setUpdate({ ...update, email: e.target.value })}
              />
            </div>

            <div className="col-md-3">
              <input
                className="form-control"
                placeholder="Product"
                value={update.product}
                onChange={(e) => setUpdate({ ...update, product: e.target.value })}
              />
            </div>

            <div className="col-md-3">
              <input
                className="form-control"
                type="number"
                placeholder="Amount"
                value={update.amount}
                onChange={(e) => setUpdate({ ...update, amount: e.target.value })}
              />
            </div>

            <div className="col-12 mt-2">
              <button
                className="btn btn-warning"
                style={{ borderRadius: 10 }}
                onClick={updateOrder}
              >
                Update
              </button>
            </div>
          </div>
        </div>

        {/* ACTIONS */}
        <div className="card mb-5 border-0 shadow-sm" style={{ borderRadius: 14 }}>
          <div className="card-header fw-semibold" style={{ background: "#f3f4ff" }}>
            Actions
          </div>

          <div className="card-body">
            <input
              className="form-control mb-2"
              placeholder="Order ID"
              value={actionId}
              onChange={(e) => setActionId(e.target.value)}
            />

            <div className="d-flex gap-2 flex-wrap">
              <button
                className="btn btn-outline-danger"
                style={{ borderRadius: 10 }}
                onClick={() => doAction("cancel")}
              >
                Cancel
              </button>

              <button
                className="btn btn-success"
                style={{ borderRadius: 10 }}
                onClick={() => doAction("confirm")}
              >
                Confirm
              </button>

              <button
                className="btn btn-dark"
                style={{ borderRadius: 10 }}
                onClick={del}
              >
                Delete
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
