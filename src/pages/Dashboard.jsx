import PageHeader from '../components/PageHeader';
import DataTable from '../components/DataTable';
import Badge from '../components/Badge';
import { BarChart, LineChart, DonutChart } from '../components/Charts';
import { movementColor, monthlyTrend, stockByCategory } from '../data/dummyData';
import { useStore } from '../store/StoreContext';

const inr = (n) => '₹' + n.toLocaleString('en-IN');

export default function Dashboard() {
  const { dashboardStats: s, lowStockProducts, stockMovements } = useStore();

  const stats = [
    { label: 'Products', value: s.totalProducts },
    { label: 'Active Warehouses', value: s.totalWarehouses },
    { label: 'Low Stock Items', value: s.lowStockCount },
    { label: 'Open Purchase Orders', value: s.openPurchaseOrders },
    { label: 'Open Sales Orders', value: s.openSalesOrders },
    { label: 'Stock Value', value: inr(s.stockValue) },
  ];

  const lowCols = [
    { key: 'sku', header: 'SKU' },
    { key: 'name', header: 'Product' },
    { key: 'onHand', header: 'On hand' },
    { key: 'reorderLevel', header: 'Reorder level' },
    {
      key: 'status',
      header: 'Status',
      render: (r) => (
        <Badge color={r.onHand === 0 ? 'red' : 'amber'}>
          {r.onHand === 0 ? 'Out of stock' : 'Low'}
        </Badge>
      ),
    },
  ];

  const moveCols = [
    { key: 'date', header: 'Date' },
    { key: 'sku', header: 'SKU' },
    { key: 'name', header: 'Product' },
    { key: 'type', header: 'Type', render: (r) => <Badge color={movementColor[r.type]}>{r.type}</Badge> },
    { key: 'quantity', header: 'Qty' },
    { key: 'ref', header: 'Reference' },
  ];

  return (
    <div>
      <PageHeader title="Dashboard" subtitle="Overview of inventory health" />

      <div className="stat-grid mb-16">
        {stats.map((st) => (
          <div className="stat" key={st.label}>
            <div className="label">{st.label}</div>
            <div className="value">{st.value}</div>
          </div>
        ))}
      </div>

      <div className="chart-grid mb-16">
        <div className="card">
          <h2>Purchases vs sales (₹ thousands)</h2>
          <LineChart data={monthlyTrend.map((m) => ({ label: m.label, value: m.sales }))} format={(v) => `₹${v}k`} />
        </div>
        <div className="card">
          <h2>Monthly purchases (₹ thousands)</h2>
          <BarChart data={monthlyTrend.map((m) => ({ label: m.label, value: m.purchases }))} format={(v) => `₹${v}k`} />
        </div>
        <div className="card">
          <h2>Stock value by category</h2>
          <DonutChart data={stockByCategory} />
        </div>
      </div>

      <div className="card mb-16">
        <h2>Low stock alerts</h2>
        <DataTable columns={lowCols} rows={lowStockProducts} empty="Everything is above reorder level 🎉" />
      </div>

      <div className="card">
        <h2>Recent stock movements</h2>
        <DataTable columns={moveCols} rows={stockMovements} />
      </div>
    </div>
  );
}
