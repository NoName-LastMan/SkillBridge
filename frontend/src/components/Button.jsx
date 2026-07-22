import React from 'react';

export default function Button({
children,
onClick,
type = 'button',
variant = 'primary',
disabled = false,
className = '',
}) {
const variants = {
primary: 'bg-blue-600 hover:bg-blue-700 text-white',
success: 'bg-green-600 hover:bg-green-700 text-white',
danger: 'bg-red-600 hover:bg-red-700 text-white',
secondary: 'bg-gray-200 hover:bg-gray-300 text-gray-800',
outline:
'border border-blue-600 text-blue-600 hover:bg-blue-50',
};

return (
<button
type={type}
onClick={onClick}
disabled={disabled}
className={`         px-5 py-3 rounded-lg font-medium transition duration-200
        disabled:opacity-50 disabled:cursor-not-allowed
        ${variants[variant]}
        ${className}
      `}
>
{children} </button>
);
}
