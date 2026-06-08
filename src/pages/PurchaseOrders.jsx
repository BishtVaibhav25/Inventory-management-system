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

export default function PurchaseOrders() {
  const { purchaseOrders, suppliers, warehouses, addPurchaseOrder } = useStore();
  const { can } = useAuth();
  const [showForm, setShowForm] = useState(false);

  const columns = [
    { key: 'id', header: 'PO #', render: (r) => `PO-${r.id}` },
    { key: 'supplier', header: 'Supplier' },
    { key: 'warehouse', header: 'Warehouse' },
    { key: 'date', header: 'Date' },
    { key: 'total', header: 'Total', render: (r) => inr(r.total) },
    { key: 'status', header: 'Status', render: (r) => <Badge color={statusColor[r.status]}>{r.status}</Badge> },
  ];

  const fields = [
    {
      name: 'supplier',
      label: 'Supplier',
      type: 'select',
      required: true,
      options: suppliers.map((s) => ({ value: s.name, label: s.name })),
    },
    {
      name: 'warehouse',
      label: 'Receiving warehouse',
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
      options: ['DRAFT', 'APPROVED', 'RECEIVED', 'CANCELLED'].map((v) => ({ value: v, label: v })),
    },
  ];

  return (
    <div>
      <PageHeader title="Purchase Orders" subtitle="Procurement — buy stock from suppliers">
        {can('create') && <button onClick={() => setShowForm(true)}>+ New PO</button>}
      </PageHeader>
      <DataTable columns={columns} rows={purchaseOrders} />

      {showForm && (
        <FormModal
          title="New Purchase Order"
          fields={fields}
          initial={{ date: today() }}
          submitLabel="Create PO"
          onSubmit={addPurchaseOrder}
          onClose={() => setShowForm(false)}
        />
      )}
    </div>
  );
}
