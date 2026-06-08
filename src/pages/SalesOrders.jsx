import { useState } from 'react';
import PageHeader from '../components/PageHeader';
import DataTable from '../components/DataTable';
import Badge from '../components/Badge';
import FormModal from '../components/FormModal';
import { statusColor } from '../data/dummyData';
import { useStore } from '../store/StoreContext';
import { useAuth } from '../auth/AuthContext';

const inr = (n) => '₹' + n.toLocaleString('en-IN');
const today = () => new Date().toISOString().slice(0, 10);

export default function SalesOrders() {
  const { salesOrders, customers, warehouses, addSalesOrder } = useStore();
  const { can } = useAuth();
  const [showForm, setShowForm] = useState(false);

  const columns = [
    { key: 'id', header: 'SO #', render: (r) => `SO-${r.id}` },
    { key: 'customer', header: 'Customer' },
    { key: 'warehouse', header: 'Warehouse' },
    { key: 'date', header: 'Date' },
    { key: 'total', header: 'Total', render: (r) => inr(r.total) },
    { key: 'status', header: 'Status', render: (r) => <Badge color={statusColor[r.status]}>{r.status}</Badge> },
  ];

  const fields = [
    {
      name: 'customer',
      label: 'Customer',
      type: 'select',
      required: true,
      options: customers.map((c) => ({ value: c.name, label: c.name })),
    },
    {
      name: 'warehouse',
      label: 'Fulfilling warehouse',
      type: 'select',
      required: true,
      options: warehouses.map((w) => ({ value: w.name, label: w.name })),
    },
    { name: 'date', label: 'Date', placeholder: today() },
    { name: 'total', label: 'Total (₹)', type: 'number', required: true, min: 0 },
    {
      name: 'status',
      label: 'Status',
      type: 'select',
      options: ['DRAFT', 'CONFIRMED', 'SHIPPED', 'CANCELLED'].map((v) => ({ value: v, label: v })),
    },
  ];

  return (
    <div>
      <PageHeader title="Sales Orders" subtitle="Fulfillment — sell & ship stock to customers">
        {can('create') && <button onClick={() => setShowForm(true)}>+ New SO</button>}
      </PageHeader>
      <DataTable columns={columns} rows={salesOrders} />

      {showForm && (
        <FormModal
          title="New Sales Order"
          fields={fields}
          initial={{ date: today() }}
          submitLabel="Create SO"
          onSubmit={addSalesOrder}
          onClose={() => setShowForm(false)}
        />
      )}
    </div>
  );
}
