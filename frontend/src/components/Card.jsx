import React from 'react';

export default function Card({
children,
className = '',
padding = 'p-5',
}) {
return (
<div
className={`         bg-white rounded-xl shadow-md border border-gray-100
        hover:shadow-lg transition duration-200
        ${padding}
        ${className}
      `}
>
{children} </div>
);
}
