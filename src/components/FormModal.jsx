import { useState } from 'react';
import Modal from './Modal';


export default function FormModal({ title, fields, initial = {}, submitLabel = 'Save', onSubmit, onClose }) {
  const [values, setValues] = useState(() => {
    const v = {};
    fields.forEach((f) => {
      v[f.name] =
        initial[f.name] ??
        (f.type === 'checkbox' ? true : f.type === 'select' ? f.options?.[0]?.value ?? '' : '');
    });
    return v;
  });
  const [errors, setErrors] = useState({});

  const set = (name, value) => setValues((prev) => ({ ...prev, [name]: value }));

  const validate = () => {
    const e = {};
    fields.forEach((f) => {
      if (f.required && f.type !== 'checkbox') {
        const val = values[f.name];
        if (val === '' || val === null || val === undefined) e[f.name] = 'Required';
      }
    });
    setErrors(e);
    return Object.keys(e).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!validate()) return;
    const out = {};
    fields.forEach((f) => {
      out[f.name] = f.type === 'number' ? Number(values[f.name]) : values[f.name];
    });
    onSubmit(out);
    onClose();
  };

  return (
    <Modal
      title={title}
      onClose={onClose}
      footer={
        <>
          <button type="button" className="secondary" onClick={onClose}>
            Cancel
          </button>
          <button type="submit" form="form-modal">
            {submitLabel}
          </button>
        </>
      }
    >
      <form id="form-modal" onSubmit={handleSubmit} className="form-grid">
        {fields.map((f) => (
          <div key={f.name} className={f.type === 'checkbox' ? 'form-field check' : 'form-field'}>
            {f.type === 'checkbox' ? (
              <label className="check-label">
                <input
                  type="checkbox"
                  checked={!!values[f.name]}
                  onChange={(e) => set(f.name, e.target.checked)}
                />
                {f.label}
              </label>
            ) : (
              <>
                <label htmlFor={f.name}>
                  {f.label}
                  {f.required && <span className="req"> *</span>}
                </label>
                {f.type === 'select' ? (
                  <select id={f.name} value={values[f.name]} onChange={(e) => set(f.name, e.target.value)}>
                    {f.options.map((o) => (
                      <option key={o.value} value={o.value}>
                        {o.label}
                      </option>
                    ))}
                  </select>
                ) : (
                  <input
                    id={f.name}
                    type={f.type === 'number' ? 'number' : f.type === 'email' ? 'email' : 'text'}
                    value={values[f.name]}
                    placeholder={f.placeholder}
                    min={f.min}
                    step={f.step}
                    onChange={(e) => set(f.name, e.target.value)}
                  />
                )}
                {errors[f.name] && <span className="field-error">{errors[f.name]}</span>}
              </>
            )}
          </div>
        ))}
      </form>
    </Modal>
  );
}
