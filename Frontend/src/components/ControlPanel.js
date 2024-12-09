import { createElement as e } from 'react';
import '../styles/ControlPanel.css';

function ControlPanel({ isRunning, onStart, onStop, onReset }) {
  return e('div', { className: 'control-panel' },
    e('h2', null, 'System Controls'),
    e('div', { className: 'system-status' },
      e('div', { 
        className: `status-indicator ${isRunning ? 'running' : 'stopped'}` 
      }, `System is ${isRunning ? 'Running' : 'Stopped'}`)
    ),
    e('div', { className: 'button-group' },
      e('button', {
        className: 'control-btn start',
        onClick: onStart,
        disabled: isRunning
      }, 'Start System'),
      e('button', {
        className: 'control-btn stop',
        onClick: onStop,
        disabled: !isRunning
      }, 'Stop System'),
      e('button', {
        className: 'control-btn reset',
        onClick: onReset,
        disabled: isRunning
      }, 'Reset System')
    )
  );
}

export default ControlPanel;
