import { createContext, useContext, useMemo, useState } from 'react';
import * as seed from '../data/dummyData';


const StoreContext = createContext(null);


const nextId = (rows) => (rows.length ? Math.max(...rows.map((r) => r.id)) + 1 : 1);

export function StoreProvider({ children }) {
  const [products, setProducts] = useState(seed.products);
  const [warehouses, setWarehouses] = useState(seed.warehouses);
  const [suppliers, setSuppliers] = useState(seed.suppliers);
  const [customers, setCustomers] = useState(seed.customers);
  const [purchaseOrders, setPurchaseOrders] = useState(seed.purchaseOrders);
  const [salesOrders, setSalesOrders] = useState(seed.salesOrders);
  const [stockLevels, setStockLevels] = useState(seed.stockLevels);
  const [stockMovements, setStockMovements] = useState(seed.stockMovements);

  const actions = useMemo(
    () => ({
      addProduct: (p) =>
        setProducts((prev) => [
          { id: nextId(prev), onHand: 0, reorderLevel: 0, active: true, ...p },
          ...prev,
        ]),
      addWarehouse: (w) =>
        setWarehouses((prev) => [{ id: nextId(prev), active: true, ...w }, ...prev]),
      addSupplier: (s) =>
        setSuppliers((prev) => [{ id: nextId(prev), active: true, ...s }, ...prev]),
      addCustomer: (c) => setCustomers((prev) => [{ id: nextId(prev), ...c }, ...prev]),
      addPurchaseOrder: (po) =>
        setPurchaseOrders((prev) => [
          { id: nextId(prev), status: 'DRAFT', ...po },
          ...prev,
        ]),
      addSalesOrder: (so) =>
        setSalesOrders((prev) => [{ id: nextId(prev), status: 'DRAFT', ...so }, ...prev]),
     
      adjustStock: ({ productId, warehouseId, sku, name, type, quantity, ref, by }) => {
        const delta = type === 'OUT' ? -Math.abs(quantity) : Math.abs(quantity);
        setStockLevels((prev) => {
          const idx = prev.findIndex(
            (s) => s.productId === productId && s.warehouseId === warehouseId
          );
          if (idx === -1) {
            return [...prev, { productId, sku, name, warehouseId, quantity: Math.max(0, delta), reserved: 0 }];
          }
          const copy = [...prev];
          copy[idx] = { ...copy[idx], quantity: Math.max(0, copy[idx].quantity + delta) };
          return copy;
        });
        setProducts((prev) =>
          prev.map((p) => (p.id === productId ? { ...p, onHand: Math.max(0, p.onHand + delta) } : p))
        );
        setStockMovements((prev) => [
          { id: nextId(prev), date: new Date().toISOString().slice(0, 16).replace('T', ' '), sku, name, type, quantity: Math.abs(quantity), ref, by },
          ...prev,
        ]);
      },
    }),
    []
  );

  
  const lowStockProducts = useMemo(
    () => products.filter((p) => p.active && p.onHand <= p.reorderLevel),
    [products]
  );

  const dashboardStats = useMemo(
    () => ({
      totalProducts: products.length,
      totalWarehouses: warehouses.filter((w) => w.active).length,
      lowStockCount: lowStockProducts.length,
      openPurchaseOrders: purchaseOrders.filter((p) => p.status !== 'RECEIVED').length,
      openSalesOrders: salesOrders.filter((s) => s.status !== 'SHIPPED').length,
      stockValue: products.reduce((sum, p) => sum + p.costPrice * p.onHand, 0),
    }),
    [products, warehouses, lowStockProducts, purchaseOrders, salesOrders]
  );

  const value = {
    products,
    warehouses,
    suppliers,
    customers,
    purchaseOrders,
    salesOrders,
    stockLevels,
    stockMovements,
    lowStockProducts,
    dashboardStats,
    ...actions,
  };

  return <StoreContext.Provider value={value}>{children}</StoreContext.Provider>;
}

export function useStore() {
  const ctx = useContext(StoreContext);
  if (!ctx) throw new Error('useStore must be used within a StoreProvider');
  return ctx;
}
