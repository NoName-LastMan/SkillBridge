import React from 'react';

export default function Input({
  label,
  type = 'text',
  value,
  onChange,
  placeholder,
  name,
  required = false,
  
  disabled = false,
}) {
  return (
    <div className='w-full'>
      {label && (
        <label className='block text-sm font-medium text-gray-700 mb-2'>
          {label}
        </label>
      )}

      <input
        type={type}
        name={name}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        required={required}
        disabled={disabled}
        className='w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition disabled:bg-gray-100'
      />
    </div>
  );
}