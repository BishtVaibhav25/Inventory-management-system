import { useState } from 'react';
import PageHeader from '../components/PageHeader';
import DataTable from '../components/DataTable';
import FormModal from '../components/FormModal';
import { useStore } from '../store/StoreContext';
import { useAuth } from '../auth/AuthContext';

export default function Inventory() {
  const { warehouses, stockLevels, products, adjustStock } = useStore();
  const { user, can } = useAuth();
  const [warehouseId, setWarehouseId] = useState(warehouses[0]?.id);
  const [showForm, setShowForm] = useState(false);

  const rows = stockLevels
    .filter((s) => s.warehouseId === Number(warehouseId))
    .map((s) => ({ ...s, available: s.quantity - s.reserved }));

  const columns = [
    { key: 'sku', header: 'SKU' },
    { key: 'name', header: 'Product' },
    { key: 'quantity', header: 'Quantity' },
    { key: 'reserved', header: 'Reserved' },
    { key: 'available', header: 'Available' },
  ];

  const fields = [
    {
      name: 'productId',
      label: 'Product',
      type: 'select',
      required: true,
      options: products.map((p) => ({ value: String(p.id), label: `${p.sku} — ${p.name}` })),
    },
    {
      name: 'warehouseId',
      label: 'Warehouse',
      type: 'select',
      required: true,
      options: warehouses.map((w) => ({ value: String(w.id), label: w.name })),
    },
    {
      name: 'type',
      label: 'Movement type',
      type: 'select',
      options: ['IN', 'OUT', 'ADJUST'].map((v) => ({ value: v, label: v })),
    },
    { name: 'quantity', label: 'Quantity', type: 'number', required: true, min: 1 },
    { name: 'ref', label: 'Reference / reason', placeholder: 'e.g. Stock count' },
  ];

  const submitAdjust = (v) => {
    const product = products.find((p) => p.id === Number(v.productId));
    adjustStock({
      productId: Number(v.productId),
      warehouseId: Number(v.warehouseId),
      sku: product?.sku,
      name: product?.name,
      type: v.type,
      quantity: v.quantity,
      ref: v.ref || 'Manual adjustment',
      by: user?.username || 'user',
    });
  };

  return (
    <div>
      <PageHeader title="Inventory" subtitle="Stock levels per warehouse">
        <select value={warehouseId} onChange={(e) => setWarehouseId(e.target.value)} style={{ width: 220 }}>
          {warehouses.map((w) => (
            <option key={w.id} value={w.id}>
              {w.name}
            </option>
          ))}
        </select>
        {can('adjust') && <button onClick={() => setShowForm(true)}>+ Adjust Stock</button>}
      </PageHeader>

      <DataTable columns={columns} rows={rows} empty="No stock recorded for this warehouse." />

      {showForm && (
        <FormModal
          title="Adjust Stock"
          fields={fields}
          initial={{ warehouseId: String(warehouseId) }}
          submitLabel="Apply Adjustment"
          onSubmit={submitAdjust}
          onClose={() => setShowForm(false)}
        />
      )}
    </div>
  );
}
