/**
 * Defines the lifecycle stages for various objects.
 * Stages of the lifecycle occur in the following order:
 * <ul>
 *     <li>
 *         Before lifetime:
 *         <ol>
 *             <li><code>Initialization</code></li>
 *         </ol>
 *     </li>
 *     <li>
 *         During lifetime, once per frame:
 *         <ol>
 *             <li><code>Input handling</code></li>
 *             <li><code>Updating</code></li>
 *             <li><code>Rendering</code></li>
 *         </ol>
 *     </li>
 *     <li>
 *         After lifetime:
 *         <ol>
 *             <li><code>Destruction</code></li>
 *         </ol>
 *     </li>
 * </ul>
 */
package dev.prozilla.pine.common.lifecycle;