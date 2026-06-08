import { useState } from 'react';
import PageHeader from '../components/PageHeader';
import DataTable from '../components/DataTable';
import Badge from '../components/Badge';
import FormModal from '../components/FormModal';
import { categories, units } from '../data/dummyData';
import { useStore } from '../store/StoreContext';
import { useAuth } from '../auth/AuthContext';

const inr = (n) => '₹' + n.toLocaleString('en-IN');

export default function Products() {
  const { products, addProduct } = useStore();
  const { can } = useAuth();
  const [search, setSearch] = useState('');
  const [showForm, setShowForm] = useState(false);

  
  const rows = products.filter(
    (p) =>
      p.name.toLowerCase().includes(search.toLowerCase()) ||
      p.sku.toLowerCase().includes(search.toLowerCase())
  );

  const columns = [
    { key: 'sku', header: 'SKU' },
    { key: 'name', header: 'Name' },
    { key: 'category', header: 'Category' },
    { key: 'unit', header: 'Unit' },
    { key: 'costPrice', header: 'Cost', render: (r) => inr(r.costPrice) },
    { key: 'sellPrice', header: 'Sell', render: (r) => inr(r.sellPrice) },
    { key: 'onHand', header: 'On hand' },
    {
      key: 'active',
      header: 'Status',
      render: (r) => <Badge color={r.active ? 'green' : 'gray'}>{r.active ? 'Active' : 'Inactive'}</Badge>,
    },
  ];

  const fields = [
    { name: 'sku', label: 'SKU', required: true, placeholder: 'e.g. ELEC-003' },
    { name: 'name', label: 'Name', required: true, placeholder: 'Product name' },
    {
      name: 'category',
      label: 'Category',
      type: 'select',
      options: categories.map((c) => ({ value: c.name, label: c.name })),
    },
    {
      name: 'unit',
      label: 'Unit',
      type: 'select',
      options: units.map((u) => ({ value: u.abbreviation, label: `${u.name} (${u.abbreviation})` })),
    },
    { name: 'costPrice', label: 'Cost price (₹)', type: 'number', required: true, min: 0 },
    { name: 'sellPrice', label: 'Sell price (₹)', type: 'number', required: true, min: 0 },
    { name: 'reorderLevel', label: 'Reorder level', type: 'number', min: 0 },
    { name: 'onHand', label: 'Opening stock', type: 'number', min: 0 },
    { name: 'active', label: 'Active', type: 'checkbox' },
  ];

  return (
    <div>
      <PageHeader title="Products" subtitle={`${rows.length} of ${products.length} shown`}>
        <input
          placeholder="Search SKU / name…"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          style={{ width: 220 }}
        />
        {can('create') && <button onClick={() => setShowForm(true)}>+ New Product</button>}
      </PageHeader>

      <DataTable columns={columns} rows={rows} empty="No products match your search." />

      {showForm && (
        <FormModal
          title="New Product"
          fields={fields}
          submitLabel="Create Product"
          onSubmit={addProduct}
          onClose={() => setShowForm(false)}
        />
      )}
    </div>
  );
}
